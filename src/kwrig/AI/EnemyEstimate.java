package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Player;
import kwrig.Score;

import java.util.*;

/**
 * Created by kwrig on 2016/02/04.
 */
public class EnemyEstimate extends EnemyDodgeAI {
    public EnemyEstimate(int n , boolean isEnemyAttack) {
        super(n,isEnemyAttack);
    }

    public EnemyEstimate(int n) {
        super(n);
    }
    static Map<Integer, Integer> collectMap = new TreeMap<>();
    static Map<Integer, Integer> countMap = new TreeMap<>();




    Field prevField = null;

    int[] changeField = new int[General.width * General.height];


    boolean estimateFlag = false;

    List<Set<Integer>> estimatePos = null;

    List<List<Integer>> changeFieldNumber = new ArrayList<>();

    @Override
    public List<Integer> action(Field field, Field ura_field, Score score) {


        if (prevField != null) {
            setChangeField(prevField, field);
            //System.out.println(changeFieldNumber.toString());
            estimatePos = EstimatePosition(field, prevField);
            if(ura_field!=null){
               // checkEstimate(ura_field);
            }

        }




        List<Integer> act = super.action(field, ura_field, score);

        Field f = new Field(field);
        f.act(act, samurAINumber);
        prevField = f;

        //System.out.println("time " + (System.currentTimeMillis() - time));

        return act;
    }



    @Override
    double getUtility(Field f, int moveTurn) {

        int util = 0;

        if (prevField == null) {
            return super.getUtility(f, moveTurn);
        }
        if(estimateFlag == false){
            return super.getUtility(f,moveTurn);
        }


        if (moveTurn == 0) {

            for (int i = 0; i < 6; i++) {

                if (i / 3 == samurAINumber / 3) {
                    continue;
                }
                if( i%3 == samurAINumber%3){
                    continue;
                }

                for (Integer pos : estimatePos.get(i)) {

                    if (f.field[pos] == samurAINumber) {
                        util += getWeight(8);
                        //util += 10 * getWeight(8);
                    } else if (General.isCanAttackRange(i, pos, f.players[samurAINumber].position)) {
                        util -= getWeight(9);
                       // util -= 11 * getWeight(9);
                    }

                }
            }
        }
        return super.getUtility(f, moveTurn) +(double)util;
    }



    void checkEstimate(Field uraField) {

        for (int i = 0; i < estimatePos.size(); i++) {

            int estimateNumber = estimatePos.get(i).size();
            if (estimateNumber == 0 || samurAINumber % 3 == i % 3) {
                continue;
            }

            if (!countMap.containsKey(estimateNumber)) {
                countMap.put(estimateNumber, 0);
                collectMap.put(estimateNumber, 0);
            }
            countMap.put(estimateNumber, countMap.get(estimateNumber) + 1);

            if (estimatePos.get(i).contains(uraField.players[i].position)) {
                collectMap.put(estimateNumber, collectMap.get(estimateNumber) + 1);
            }
        }

        Set<Integer> keySet = countMap.keySet();

        System.out.println("正答率表");
        for (Integer i : keySet) {


            System.out.print("[ " + i + " : " + collectMap.get(i) + " / " + countMap.get(i) + " ]");

        }
        System.out.println();


    }


    private List<Set<Integer>> EstimatePosition(Field f, Field prev) {

        estimateFlag = false;

        List<Set<Integer>> ret = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            ret.add(new HashSet<>());
        }

        int attackPoscount = 0;

        List<Set<Integer>> estimateAttackPos = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            estimateAttackPos.add(new HashSet<>());

            if (i / 3 == samurAINumber / 3) {
                continue;
            }
            if (f.players[i].isHide == false) {
                continue;
            }

            if (changeFieldNumber.get(i).size() == 0) {
                continue;
            }

            /*
            攻撃地点の予測を行う
             */


            for (int pos = 0; pos < General.width * General.height; pos++) {

                int[] pp = General.getXY(pos);

                if(General.getDist(pos , changeFieldNumber.get(i).get(0)) > 5){
                    continue;
                }

                for (int dist = 0; dist < 4; dist++) {

                    int[][] attackRange = General.getAttackRange(i, dist);

                    boolean flag = true;

                    for (Integer p : changeFieldNumber.get(i)) {

                        int[] changePos = General.getXY(p);

                        int[] distPos = {changePos[0] - pp[0], changePos[1] - pp[1]};

                        if (containt(attackRange, distPos) == false) {
                            flag = false;
                            break;
                        }
                    }

                    if (flag == true) {

                        for (int[] attackPosDist : attackRange) {

                            int[] attackPos = {pp[0] + attackPosDist[0], pp[1] + attackPosDist[1]};

                            if (!General.isRangeX(attackPos[0]) || !General.isRangeY(attackPos[1])) {
                                continue;
                            }
                            int color = f.field[General.getPosition(attackPos[0], attackPos[1])];
                            if (color != 9 && color != i) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (flag == true) {
                        estimateAttackPos.get(i).add(pos);
                        attackPoscount++;
                    }
                }
            }

        }
        // System.out.println("攻撃予測位置 " + attackPoscount );


        /*
        攻撃地点から今いる位置を推定
         */
        for (int i = 0; i < 6; i++) {

            if (i / 3 == samurAINumber / 3) {
                continue;
            }

            Set<Integer> attackPos = estimateAttackPos.get(i);
            if (attackPos.size() == 0) {
                continue;
            }

            //それぞれの予測攻撃位置について
            /*
            memo 現れて攻撃して移動して隠れるはできない
             */

            for (int pos : attackPos) {

                //前回のプレイヤーが隠れてなければ、とれる行動は移動攻撃or攻撃移動→hide
                if (prev.players[i].isHide == false) {

                    //前回の位置と攻撃位置が同一なら攻撃→(移動)→hide
                    if (pos == prev.players[i].position) {

                        if (f.field[pos] == i || f.field[pos] == 9) {
                            ret.get(i).add(pos);
                        }

                        for (int dist = 0; dist < 4; dist++) {

                            Player player = new Player(prev.players[i]);
                            player.move(dist, f);
                            if (f.field[player.position] / 3 == player.samuraiNumber / 3) {
                                ret.get(i).add(player.position);
                            }
                        }
                    }
                    //前回の位置と攻撃位置が1ます差なら攻撃位置
                    if (General.getDist(pos, prev.players[i].position) == 1) {
                        if (f.field[pos] == i || f.field[pos] == 9) {
                            ret.get(i).add(pos);
                        }
                    }


                } else {
                    //前回のプレイヤーが隠れていれば攻撃位置に隠れているor視界の外
                    //or視界の外からやってきてhide

                    //攻撃位置が隠れられるなら隠れているかも
                    if (f.field[pos] == i || f.field[pos] == 9) {
                        ret.get(i).add(pos);
                    }


                    //攻撃位置が5ますめのところなら6マス目にいるかも
                    if (General.getDist(pos, f.players[samurAINumber].position) == 5) {
                        for (int dist = 0; dist < 4; dist++) {
                            Player player = new Player();
                            player.position = pos;
                            player.move(dist, f);
                            if (General.getDist(player.position, f.players[samurAINumber].position) == 6) {
                                ret.get(i).add(player.position);
                            }
                        }
                    }

                    //攻撃位置が6ますよりも遠いならそこから攻撃移動がある
                    if (General.getDist(pos, f.players[samurAINumber].position) > 5) {
                        for (int dist = 0; dist < 4; dist++) {
                            Player player = new Player();
                            player.position = pos;
                            player.move(dist, f);
                            if (General.getDist(player.position, f.players[samurAINumber].position) > 5) {
                                ret.get(i).add(player.position);
                            }
                        }
                    }


                }
            }
        }

        for(int i = 0; i < 6; i++) {
            if( i/3 == samurAINumber/3){
                continue;
            }
            if(ret.get(i).size() >0){
                estimateFlag = true;
            }
        }

        return ret;
    }


    private boolean containt(int[][] list, int[] target) {

        for (int[] ll : list) {
            if (ll[0] == target[0] && ll[1] == target[1]) {
                return true;
            }
        }
        return false;
    }

    private void setChangeField(Field prev, Field now) {

        int[] prevField = prev.field;
        int[] nowField = now.field;


        changeFieldNumber = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            changeFieldNumber.add(new ArrayList<>());
        }


        for (int i = 0; i < changeField.length; i++) {

            if (prevField[i] == 9 || nowField[i] == 9) {
                changeField[i] = 9;
                continue;
            }
            if (prevField[i] != nowField[i]) {

                if(nowField[i] > 6){
                    continue;
                }

                changeField[i] = nowField[i];
                changeFieldNumber.get(nowField[i]).add(i);
            } else {
                changeField[i] = 8;
            }
        }
    }


    @Override
    public String getName() {
        return "EnemyEstimateN=" + getN()+"attack"+ isEnemyAttack + "AI";
    }
}
