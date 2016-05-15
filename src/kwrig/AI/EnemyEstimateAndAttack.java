package kwrig.AI;

import kwrig.Field;
import kwrig.General;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/02/06.
 */
public class EnemyEstimateAndAttack extends EnemyEstimate {
    public EnemyEstimateAndAttack(int n, boolean isEnemyAttack) {
        super(n, isEnemyAttack);
    }

    public EnemyEstimateAndAttack(int n) {
        super(n);
    }

    public EnemyEstimateAndAttack(int n , boolean isEnemyAttack , int w){
        super(n,isEnemyAttack);
        DataStrage strage = new DataStrage();
        Weights = strage.getWeights(w);
        this.w = w;
    }
    int w = -1;

    /*
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


                List<Integer> dist = new ArrayList<>(3);
                int[] mypos = General.getXY(f.players[samurAINumber].position);
                int[] enemypos;

                if(f.players[i].isHide==false){
                    enemypos = General.getXY(f.players[i].position);

                    dist.add(enemypos[0] - mypos[0]);
                    dist.add(enemypos[1]-mypos[1]);

                    if(General.NEARATTACKRANGE.get(samurAINumber%3).contains(dist)){
                        util += 10;
                    }
                }


                for (Integer pos : estimatePos.get(i)) {

                    if (f.field[pos] == samurAINumber) {
                        util += 10;
                    } else if (General.isCanAttackRange(i, pos, f.players[samurAINumber].position)) {
                        util -= 20;
                    }

                    enemypos = General.getXY(pos);
                    dist.add(enemypos[0] - mypos[0]);
                    dist.add(enemypos[1]-mypos[1]);
                    if(General.NEARATTACKRANGE.get(samurAINumber%3).contains(dist)){
                        util += 5;
                    }


                }
            }
        }

        if(moveTurn == 1 && General.leaveMoveTurnInEqualBuki(f.turn , samurAINumber)==1){

            util += f.score.death[ (samurAINumber+3)%6 ] * 100;

        }


        return super.getUtility(f, moveTurn) +(double)util;
    }
    */

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


                List<Integer> dist = new ArrayList<>(3);
                int[] mypos = General.getXY(f.players[samurAINumber].position);
                int[] enemypos;

                if(f.players[i].isHide==false){
                    enemypos = General.getXY(f.players[i].position);

                    dist.add(enemypos[0] - mypos[0]);
                    dist.add(enemypos[1]-mypos[1]);

                    if(General.NEARATTACKRANGE.get(samurAINumber%3).contains(dist)){
                        if(f.players[samurAINumber].isHide){
                            util += getWeight(10);
                            //util += 10.0 * getWeight(10);
                        }else {
                            util += getWeight(11);
                            //util += 5.0 * getWeight(11);
                        }
                    }
                }


                for (Integer pos : estimatePos.get(i)) {

                    enemypos = General.getXY(pos);
                    dist.add(enemypos[0] - mypos[0]);
                    dist.add(enemypos[1]-mypos[1]);
                    if(General.NEARATTACKRANGE.get(samurAINumber%3).contains(dist)){
                        if(f.players[samurAINumber].isHide){
                            util += getWeight(12) / estimatePos.size();
                           // util += 10.0/estimatePos.size() * getWeight(12);
                        }else {
                            util += getWeight(13)/estimatePos.size();
                            //util += 5.0/estimatePos.size() * getWeight(13);
                        }
                    }
                }
            }
        }

        if(moveTurn == 1 && General.leaveMoveTurnInEqualBuki(f.turn , samurAINumber)==1 && f.players[(samurAINumber+3)%6].isHide==false){

            util += f.score.death[ (samurAINumber+3)%6 ] * getWeight(14);
         //   util += f.score.death[ (samurAINumber+3)%6 ] * 100 * getWeight(14);

            int[] mypos = General.getXY(f.players[samurAINumber].position);
            int[] enemypos = General.getXY(f.players[(samurAINumber+3)%6 ].position);
            int[] ddd = {enemypos[0] - mypos[0] , enemypos[1]-mypos[1]};

            boolean flag = true;
            for(List<Integer> dist : General.NEARATTACKRANGE.get(samurAINumber%3)){

                int[] dd = {dist.get(0) , dist.get(1)};
                if( Math.abs(dd[0]-ddd[0]) + Math.abs(dd[1]-ddd[1]) == 1 && flag == true){
                    flag = false;

                    util -= getWeight(15);
                    //util -= 20 * getWeight(15);
                }
            }
        }


        return super.getUtility(f, moveTurn) +(double)util;
    }

    @Override
    public String getName() {
        return "AttackAIattack" + isEnemyAttack + " W=" + w +"  ";
    }
}
