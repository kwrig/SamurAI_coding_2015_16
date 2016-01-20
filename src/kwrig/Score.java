package kwrig;

import kwrig.AI.AI;
import kwrig.manager.AIResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kwrig on 2016/01/04.
 * スコアクラス
 * Fieldを渡してそれぞれのスコアを算出
 */

public class Score {

    public int[] AINumber = new int[6];

    public int[] teamPoint = new int[2];

    public int[] playerPoint = new int[6];

    public int[] kill = new int[6];
    public int[] death = new int[6];

    //フィールド点のみ
    public void calcScore(Field f){

        teamPoint = new int[2];
        playerPoint = new int[6];

        for(Integer i : f.field){

            if(i<6){
                playerPoint[i]++;
                teamPoint[i/3]++;
            }
        }
    }

    public void copy(Score score){

        teamPoint = Arrays.copyOf(score.teamPoint,score.teamPoint.length);
        playerPoint = Arrays.copyOf(score.playerPoint,score.playerPoint.length);
        kill = Arrays.copyOf(score.kill,score.kill.length);
        death = Arrays.copyOf(score.death,score.death.length);
        AINumber = Arrays.copyOf(score.AINumber,score.AINumber.length);


    }




    public Score(){

    }

    public Score(Score score){
        copy(score);
    }

    public int getSamuraiNumber(AI ai){

        int aiNumber = ai.getAINumber();

        int i=0;

        for (i=0;i<AINumber.length;i++){

            if(AINumber[i] == aiNumber){
                return i;
            }

        }

        System.err.println("AI Number not found");
        return -1;
    }

    public int getPoint(int samuraiNumber){

        int ret = playerPoint[samuraiNumber];

        if(teamPoint[samuraiNumber/3] > teamPoint[1-samuraiNumber/3]){
            ret +=300;
        }

        return ret;
    }

    public List<AIResult> getAIResults(){

        List<AIResult> results = new ArrayList<AIResult>();

        for (int i = 0; i < 6; i++) {

            AIResult result = new AIResult();
            result.setAInumber(AINumber[i]);
            result.setPoint(getPoint(i));
            result.setDeath(death[i]);
            result.setKill(kill[i]);
            results.add(result);
        }
        return results;

    }


}
