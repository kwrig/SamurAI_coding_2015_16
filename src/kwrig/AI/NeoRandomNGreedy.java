package kwrig.AI;

import kwrig.Field;
import kwrig.Score;

/**
 * Created by kwrig on 2016/01/09.
 */
public class NeoRandomNGreedy extends RandomNGreedy {

    public NeoRandomNGreedy(int n){
        N = n;
    }

    @Override
    int getUtility(Field f) {

        int util = super.getUtility(f);

        Score score = f.score;
        score.calcScore(f);

        util += score.kill[samurAINumber] * 10;

        util += score.teamPoint[samurAINumber/3];


        return util;
    }

    @Override
    public String getName(){
        return "NeoRandomN=" + getN() + "Greedy";
    }
}
