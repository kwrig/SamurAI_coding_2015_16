package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Player;
import kwrig.Score;

import java.util.*;

/**
 * Created by kwrig on 2016/02/22.
 */
public class NeoEnemyEstimate extends EnemyDodgeAI {

    public NeoEnemyEstimate(int n) {
        super(n, true);
        DataStrage strage = new DataStrage();
        Weights = strage.getWeights(0, true);
        num = 0;
    }

    public NeoEnemyEstimate(int n, int d, boolean useKillList) {
        super(n, true);
        DataStrage strage = new DataStrage();

        Weights = strage.getWeights(d, true);
        num = d;

        this.useKillList = useKillList;

    }

    boolean useKillList = false;

    int num = 0;

    Map<Integer, Set<Integer>> enemyPosition = new HashMap<>();

    Map<Integer, Integer> enemyEstimateTurn = new HashMap<>();
    Field prevField = null;

    int[] killerTurnList = new int[6];

    List<Set<Integer>> changeFieldNumber = new ArrayList<>();
    int[] changeField = new int[General.width * General.height];


    @Override
    double getUtility(Field f, int moveTurn) {
        double util = 0;

        Score score = f.score;
        score.calcScore(f);

        if (prevField == null) {
            return super.getUtility(f, moveTurn);
        }
        int hhh = 23;

        int[] mypos = General.getXY(f.players[samurAINumber].position);
        int[] enemypos;
        List<Integer> dist;
        if (moveTurn == 0) {

            for (int i = 0; i < 6; i++) {

                if (i / 3 == samurAINumber / 3) {
                    continue;
                }

                if (!enemyPosition.containsKey(i)) {
                    continue;
                }

                if (useKillList == true && killerTurnList[i] > 0) {
                    continue;
                }


                Set<Integer> estimatePos = enemyPosition.get(i);

                int counter = Math.min(enemyEstimateTurn.get(i), 4);
                for (Integer pos : estimatePos) {


                    //特定の相手に対しては2回行動を許す場合があって相手に近いと減点
                    if (i % 3 == samurAINumber % 3 && General.leaveMoveTurnInEqualBuki(f.turn + 1, samurAINumber) == 0) {
                        enemypos = General.getXY(pos);
                        dist = new ArrayList<>(3);
                        dist.add(enemypos[0] - mypos[0]);
                        dist.add(enemypos[1] - mypos[1]);
                        if (General.NEARNEARATTACKRANGE.get(samurAINumber % 3).contains(dist)) {
                            if (f.players[samurAINumber].isHide) {
                                if (counter == 0) {
                                    util -= getWeight(0);

                                } else {
                                    util -= getWeight(1) * getWeight(hhh + counter) / estimatePos.size();
                                }
                            } else {
                                if (counter == 0) {
                                    util -= getWeight(2);

                                } else {
                                    util -= getWeight(3) * getWeight(hhh + counter) / estimatePos.size();
                                }
                            }
                        }
                    }


                    //推定領域を攻撃した場合の点
                    if (f.field[pos] == samurAINumber) {
                        if (counter == 0) {
                            util += score.death[i] * getWeight(4);

                        } else {
                            util += getWeight(5) * getWeight(hhh + counter) / estimatePos.size();
                        } //util += 10 * getWeight(8);
                        // System.out.println("hit " + util);

                        //推定領域から攻撃が可能かどうか //自分が隠れているかどうかで2種類
                    } else if (General.isCanAttackRange(i, pos, f.players[samurAINumber].position)) {

                        if (f.players[samurAINumber].isHide) {
                            if (counter == 0) {
                                util -= getWeight(6);

                            } else {
                                util -= getWeight(7) * getWeight(hhh + counter) / estimatePos.size();
                            }
                        } else {
                            if (counter == 0) {
                                util -= getWeight(8);

                            } else {
                                util -= getWeight(9) * getWeight(hhh + counter) / estimatePos.size();
                            }
                        }

                        //推定領域から一歩動いたら攻撃可能かどうか //自分が隠れているかどうかで2種類
                    } else {
                        enemypos = General.getXY(pos);
                        dist = new ArrayList<>(3);
                        dist.add(enemypos[0] - mypos[0]);
                        dist.add(enemypos[1] - mypos[1]);
                        if (General.NEARATTACKRANGE.get(samurAINumber % 3).contains(dist)) {
                            if (f.players[samurAINumber].isHide) {
                                if (counter == 0) {
                                    util += getWeight(10);

                                } else {
                                    util += getWeight(11) * getWeight(hhh + counter) / estimatePos.size();
                                }
                            } else {
                                if (counter == 0) {
                                    util += getWeight(12);

                                } else {
                                    util += getWeight(13) * getWeight(hhh + counter) / estimatePos.size();
                                }
                            }
                        } else {

                            if (Math.abs(dist.get(0)) + Math.abs(dist.get(1)) <= 5) {
                                if (f.players[samurAINumber].isHide) {
                                    if (counter == 0) {
                                        util -= getWeight(14);

                                    } else {
                                        util -= getWeight(15) * getWeight(hhh + counter) / estimatePos.size();
                                    }
                                } else {
                                    if (counter == 0) {
                                        util -= getWeight(16);

                                    } else {
                                        util -= getWeight(17) * getWeight(hhh + counter) / estimatePos.size();
                                    }
                                }
                            }

                        }


                    }
                }
            }

            if (f.players[samurAINumber].isHide) {
                if (General.leaveMoveTurnInEqualBuki(f.turn + 1, samurAINumber) == 0) {
                    util += getWeight(18);
                } else {
                    util += getWeight(19);
                }
            }


        }
        if (moveTurn == 1 && General.leaveMoveTurnInEqualBuki(f.turn + 1, samurAINumber) == 1) {

            int samurai = (samurAINumber + 3) % 6;
            if (enemyPosition.containsKey(samurai)) {
                Set<Integer> estimatePos = enemyPosition.get(samurai);

                int counter = Math.min(enemyEstimateTurn.get(samurai), 4);
                for (Integer pos : estimatePos) {
                    //推定領域を攻撃した場合の点

                    if (counter == 0) {
                        util += score.death[samurai] * getWeight(4);
                    } else {
                        util += getWeight(5) * getWeight(hhh + counter) / estimatePos.size();
                    }
                }
            }
        }


        if (moveTurn == getN() - 1) {

            util += score.teamPoint[samurAINumber / 3] * getWeight(20);
            util += score.playerPoint[samurAINumber] * getWeight(21);

            if (isEnemyAttack) {
                util -= score.teamPoint[1 - samurAINumber / 3] * getWeight(22);
            }
            double dd = General.getDist(f.players[samurAINumber].position, General.getPosition(7, 7));
            util -= dd * getWeight(23);
        }

        return util;

    }

    private void calcEnemyPosition(Field f, Field prev) {

        List<Set<Integer>> estimatePos = EstimatePosition(f, prev);

        if (ura != null) {
            //checkEstimate(ura,estimatePos);
        }

        //   System.out.println("calc");
        //System.out.println(estimatePos.toString());

        for (int i = 0; i < 6; i++) {
            //int samurai = i + (1 - samurAINumber / 3) * 3;
            int samurai = i;

            // System.out.println(enemyEstimateTurn.getOrDefault(samurai,-1));
            //System.out.println(enemyPosition.getOrDefault(samurai,new HashSet<>()).toString());

            if (samurai / 3 == samurAINumber / 3) {
                continue;
            }

            if (samurai % 3 == samurAINumber % 3 && enemyPosition.containsKey(samurai)) {

                if (General.leaveMoveTurnInEqualBuki(f.turn + 1, samurAINumber) == 0) {
                    continue;
                }

                Set<Integer> next_est = getMovedPosition(enemyPosition.get(samurai), 3);
                Set<Integer> next = new HashSet<>();
                for (Integer pos : next_est) {
                    if (f.field[pos] / 3 == samurai / 3 || f.field[pos] == 9) {
                        next.add(pos);
                    }
                }
                enemyEstimateTurn.put(samurai, enemyEstimateTurn.get(samurai) + 1);
            }

            if (f.players[samurai].isHide == false) {
                enemyEstimateTurn.put(samurai, 0);
                Set<Integer> pos = new HashSet<>();
                pos.add(f.players[samurai].position);
                enemyPosition.put(samurai, pos);
                continue;
            }

            if (estimatePos.get(samurai).size() > 0) {

                if (samurai % 3 == samurAINumber % 3) {
                    enemyPosition.put(samurai, estimatePos.get(samurai));
                    enemyEstimateTurn.put(samurai, 1);
                } else {

                    enemyPosition.put(samurai, estimatePos.get(samurai));
                    enemyEstimateTurn.put(samurai, 1);
                    continue;
                }
            }

            if (enemyPosition.containsKey(samurai)) {

                Set<Integer> next_est = getMovedPosition(enemyPosition.get(samurai), 3);

                Set<Integer> next = new HashSet<>();

                for (Integer pos : next_est) {
                    if (f.field[pos] / 3 == samurai / 3 || f.field[pos] == 9) {
                        next.add(pos);
                    }
                }
                enemyEstimateTurn.put(samurai, enemyEstimateTurn.get(samurai) + 1);
                enemyPosition.put(samurai, next);
            }


        }
    }

    Set<Integer> getMovedPosition(Set<Integer> prevPos, int move) {

        Set<Integer> ret = new HashSet<>();
        for (int i = 0; i < General.width * General.height; i++) {

            for (Integer pos : prevPos) {
                if (General.getDist(pos, i) <= move) {
                    ret.add(i);
                    continue;
                }
            }
        }
        return ret;
    }


    private List<Set<Integer>> EstimatePosition(Field f, Field prev) {

        List<Set<Integer>> ret = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            ret.add(new HashSet<>());
        }
        List<Set<Integer>> estimateAttackPos = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            estimateAttackPos.add(new HashSet<>());

            if (i / 3 == samurAINumber / 3) {
                continue;
            }
            if (f.players[i].isHide == false) {
                continue;
            }
            if (i % 3 == samurAINumber % 3) {
                //  continue;
            }

            if (changeFieldNumber.get(i).size() == 0) {
                continue;
            }

            /*
            攻撃地点の予測を行う
            攻撃範囲のますが全部塗られているか不明(or味方に塗られた)ならそこが攻撃地点とする
             */


            for (int pos = 0; pos < General.width * General.height; pos++) {

                int[] pp = General.getXY(pos);


                for (int dist = 0; dist < 4; dist++) {


                    int[][] attackRange = General.getAttackRange(i, dist);

                    boolean flag = true;

                    boolean attackFlag = false;

                    int count = 0;
                    for (int a = 0; a < attackRange.length; a++) {

                        if (!General.isRangeX(pp[0] + attackRange[a][0]) || !General.isRangeY(pp[1] + attackRange[a][1])) {
                            continue;
                        }
                        int p = General.getPosition(pp[0] + attackRange[a][0], pp[1] + attackRange[a][1]);

                        if (changeField[p] == i) {
                            attackFlag = true;
                            count++;

                            //  } else if (changeField[p] == 8 || f.field[p]==i) {
                        } else if (f.field[p] != 9 && f.field[p] != i) {
                            flag = false;
                            break;
                        }
                    }

                    if (i % 3 != samurAINumber % 3 && changeFieldNumber.get(i).size() != count) {
                        flag = false;
                    }

                    if (flag == true && attackFlag == true) {
                        estimateAttackPos.get(i).add(pos);
                    }
                }
            }
        }


        /*
        攻撃地点から今いる位置を推定
         */
        for (int i = 0; i < 6; i++)

        {

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

        return ret;
    }


    private void setChangeField(Field prev, Field now) {

        int[] prevField = prev.field;
        int[] nowField = now.field;


        changeFieldNumber = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            changeFieldNumber.add(new HashSet<>());
        }


        for (int i = 0; i < changeField.length; i++) {

            if (prevField[i] == 9 || nowField[i] == 9) {
                changeField[i] = 9;
                continue;
            }
            if (prevField[i] != nowField[i]) {

                if (nowField[i] > 6) {
                    continue;
                }

                changeField[i] = nowField[i];
                changeFieldNumber.get(nowField[i]).add(i);
            } else {
                changeField[i] = 8;
            }
        }
    }


    private boolean containt(int[][] list, int[] target) {

        for (int[] ll : list) {
            if (ll[0] == target[0] && ll[1] == target[1]) {
                return true;
            }
        }
        return false;
    }

    Field ura = null;

    @Override
    public List<Integer> action(Field field, Field ura_field, Score score) {


        if (ura_field != null) {
            ura = ura_field;
        }

        if (prevField != null) {
            setChangeField(prevField, field);
            //System.out.println(changeFieldNumber.toString());
            calcEnemyPosition(field, prevField);

            if(useKillList) {
                checkKilled(field, prevField);
            }
        }


        List<Integer> act = super.action(field, ura_field, score);

        Field f = new Field(field);
        f.act(act, samurAINumber);

        if (f.players[samurAINumber].health == 0) {
            /*
            System.out.println("util  " + getUtility(f, 0));
            if(getUtility(f, 0)>1000){
                System.out.println("field");
                System.out.println(field.toString());

                System.out.println("esr_next");
                System.out.println(f.toString());


            }
            */
        }

        prevField = f;

        //System.out.println("time " + (System.currentTimeMillis() - time));

        return act;
    }

    void checkKilled(Field now, Field prev) {


        for (int i = 0; i < 6; i++) {

            int home = now.players[i].homePosition;


            if (killerTurnList[i] > 0) {
                now.players[i].position = home;
                now.players[i].isHide = false;
            }

            killerTurnList[i] = Math.max(0, killerTurnList[i] - 1);

            if (prev.field[home] == i && now.field[home] == i) {

                if (prev.players[i].position != prev.players[i].homePosition &&
                        now.players[i].position == now.players[i].homePosition) {
                    killerTurnList[i] = 2;
                }
            }


        }
    }


    @Override
    double getWeight(int n) {
        return Weights.get(n);
    }

    @Override
    public String getName() {
        return "NeoEnemyEstimate d=" + num + " kuse=" + useKillList + " ";
    }

    static Map<Integer, Integer> collectMap = new TreeMap<>();
    static Map<Integer, Integer> countMap = new TreeMap<>();

    void checkEstimate(Field uraField, List<Set<Integer>> estimatePos) {

        for (int i = 0; i < 6; i++) {

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
}
