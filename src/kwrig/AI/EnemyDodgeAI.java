package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Player;
import kwrig.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/02/01.
 */
public class EnemyDodgeAI extends ActionListGreedyAI {


    public List<Double> Weights = new ArrayList<>(510);
    int bukiNumber = 0;

    public EnemyDodgeAI(int n ,boolean IsenemyAttack) {
        super(n);
        isEnemyAttack = IsenemyAttack;
        init();
    }

    public EnemyDodgeAI(int n) {
        super(n);
        init();
    }

    private void init(){
        DataStrage strage = new DataStrage();

        Weights = strage.getWeights();

        bukiNumber = samurAINumber%3;
    }


    boolean isEnemyAttack = false;

    Score firstScore = null;

    @Override
    public List<Integer> action(Field field, Field ura_field, Score score) {

        firstScore = field.score;
        firstScore.calcScore(field);
        return super.action(field, ura_field, score);
    }

    double getWeight(int n){
        return Weights.get(n*3 + bukiNumber);
    }

    @Override
    double getUtility(Field f, int moveTurn) {
        double util =10;

        Score score = f.score;
        score.calcScore(f);

        //util += score.playerPoint[samurAINumber];


        if(moveTurn==0){

            if(General.leaveMoveTurnInEqualBuki(f.turn+1 , samurAINumber) == 0) {
                util += score.teamPoint[samurAINumber / 3] * getWeight(0);
                //util += score.teamPoint[samurAINumber/3]/5.0 * getWeight(0);
            }


            util += score.kill[samurAINumber] * getWeight(1);
            //util += score.kill[samurAINumber] * 300 * getWeight(1);
            if(f.players[samurAINumber].isHide){
                util += getWeight(2);
                //util += 0.01 * getWeight(2);
            }

            for(Player player : f.players){

                if(player.samuraiNumber/3 == samurAINumber/3){
                    continue;
                }
                if(player.isHide){
                    continue;
                }
                if(General.isCanAttackRange(player.samuraiNumber , player.position , f.players[samurAINumber].position)){
                    util -= getWeight(3);
                    //util -= 200 * getWeight(3);
                }
            }

        }

        if(moveTurn == getN()-1){
            // util += score.playerPoint[samurAINumber];

            util += score.teamPoint[samurAINumber/3] * getWeight(4);
            //util += score.teamPoint[samurAINumber/3] * getWeight(4);
            util += score.playerPoint[samurAINumber]* getWeight(5);
            //util += score.playerPoint[samurAINumber] / 10.0 * getWeight(5);

            double dist = General.getDist(f.players[samurAINumber].position , General.getPosition(7,7));
            util -= dist * getWeight(6);
            //util -= dist/4.001 * getWeight(6);

            if(isEnemyAttack){
                util -= score.teamPoint[1-samurAINumber/3] * getWeight(7);
            }

        }

        return util;
    }

    @Override
    public String getName() {
        return "EnemyDodgeN=" + getN()+"attack" +isEnemyAttack +"AI";
    }



}
