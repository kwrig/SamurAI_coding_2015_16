package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/03.
 */
public class RandomAI extends AI {





    @Override
    public List<Integer> action(Field field, Field ura_field, Score score) {
        return getRandomAct();
    }



    public List<Integer> getRandomAct(){
        List<Integer> ret = new ArrayList<Integer>();

        int cost = 0;

        while (true){

            int move = (int)(Math.random() * 10) + 1;

            cost += General.COST[move];
            if(cost > 7){
                break;
            }
            ret.add(move);
        }

        ret.add(0);

        return ret;

    }

    @Override
    public String getName() {
        return "RandomAI";
    }

    @Override
    public void EndCall(Score score) {

    }
}
