package kwrig.AI;

import kwrig.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/04.
 */
public class RandomGreedyAI extends RandomAI {

    int simNumber = 10000;

    @Override
    public List<Integer> action(Field field) {


        Field buf = new Field(field);

        List<Integer> ret = new ArrayList<Integer>();
        ret.add(0);
        int maxutil = 0;

        for (int i = 0; i < simNumber; i++) {

            buf.copy(field);

            List<Integer> act = getRandomAct();

            buf.act(act , samurAINumber);

            int util = getUtility(buf);

            if(util > maxutil){
                maxutil = util;
                ret = act;
            }
        }

        //System.out.println("util " + maxutil);


        return ret;
    }



    int getUtility(Field f){

        int ret=0;
        int len = f.field.length;
        for (int i = 0; i < len; i++) {

            if(f.field[i] == samurAINumber){
                ret++;
            }

        }
        return ret;
    }


    @Override
    public String getName() {
        return "RandomGreedy";
    }
}


