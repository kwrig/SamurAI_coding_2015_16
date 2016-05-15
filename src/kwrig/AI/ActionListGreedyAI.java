package kwrig.AI;

import kwrig.Field;
import kwrig.Score;

import java.util.List;

/**
 * Created by kwrig on 2016/01/31.
 */
public class ActionListGreedyAI extends RandomNGreedy {





    public ActionListGreedyAI(int n) {
        super(n);
        for(int i = 0; i < 6; i++) {
          bufs[i] = new Field();
        }

    }

    ActionList actionList = new ActionList();


    Field bufs[] = new Field[6];



    @Override
    public List<Integer> action(Field field, Field ura_field, Score score) {

        Field buf = bufs[0];

        List<Integer> maxAction = null;
        double maxUtil = Integer.MIN_VALUE;

        List<List<Integer>> actions;

        if(field.players[samurAINumber].isHide){
            actions = actionList.hiddenAllList;
        }else{
            actions = actionList.openAllList;
        }
        for(List<Integer> list : actions){
            buf.copy(field);
            if (buf.act(list , samurAINumber) == false){
                continue;
            }
            double util = getUtility(buf , 0) + ReAction(buf,1);
            if(util >= maxUtil){
                maxUtil = util;
                maxAction = list;
            }
        }
        return maxAction;
    }

    double ReAction(Field field , int moved){

        if(moved >= getN()){
            return 0;
        }


        Field buf = bufs[moved];

        List<List<Integer>> actions;

        if(moved == getN()-1) {
            if (field.players[samurAINumber].isHide) {
                actions = actionList.hiddenAttackAllList;
            } else {
                actions = actionList.openAttackAllList;
            }
        }else {

            if (field.players[samurAINumber].isHide) {
                actions = actionList.hiddenList;
            } else {
                actions = actionList.openMaxList;
            }
        }

        double maxutil = Double.MIN_VALUE;

        for(List<Integer> list : actions){
            buf.copy(field);
            if(buf.act(list , samurAINumber)) {
                maxutil = Math.max(getUtility(buf, moved) + ReAction(buf, moved + 1), maxutil);
            }
        }

        return maxutil;
    }





    double getUtility(Field f , int moveTurn) {

        int util =0;

        Score score = f.score;
        score.calcScore(f);

        util += score.playerPoint[samurAINumber];
        util += score.teamPoint[samurAINumber/3];


        if(moveTurn==0){
            util += score.kill[samurAINumber] * 30;
            if(f.players[samurAINumber].isHide){
                util += 2;
            }
        }

        if(moveTurn == getN()-1){
            util += score.playerPoint[samurAINumber];
            util += score.teamPoint[samurAINumber/3];
        }

        return util;
    }


    @Override
    public String getName(){
        return "ActionListN=" + getN() + "Greedy";
    }


}
