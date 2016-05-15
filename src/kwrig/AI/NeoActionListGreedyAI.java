package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Score;

/**
 * Created by kwrig on 2016/01/31.
 */
public class NeoActionListGreedyAI extends ActionListGreedyAI {
    public NeoActionListGreedyAI(int n) {
        super(n);
    }


    @Override
    double getUtility(Field f, int moveTurn) {
        int util =0;

        Score score = f.score;
        score.calcScore(f);

        //util += score.playerPoint[samurAINumber];
        util += score.teamPoint[samurAINumber/3];



        if(moveTurn==0){
            util += score.kill[samurAINumber] * 30;
            if(f.players[samurAINumber].isHide){
                util += 0.1;
            }
        }

        if(moveTurn == getN()-1){
           // util += score.playerPoint[samurAINumber];
            util += score.teamPoint[samurAINumber/3];

            double dist = General.getDist(f.players[samurAINumber].position , f.players[(samurAINumber+3)%6].homePosition);
            util -= dist/2.001;

        }

        return util;
    }

    @Override
    public String getName() {
        return "NeoActionListN=" + getN();
    }
}
