package kwrig.AI;

import kwrig.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/09.
 */
public class RandomNGreedy extends RandomGreedyAI {

    int N = 2;

    public RandomNGreedy(int n){
        N = n;
    }
    public RandomNGreedy(){
    }


    @Override
    public List<Integer> action(Field field) {


        Field buf = new Field(field);

        List<Integer> ret = new ArrayList<Integer>();
        ret.add(0);
        int maxutil = 0;

        for (int i = 0; i < simNumber; i++) {

            buf.copy(field);

            List<Integer> act = getRandomAct();
            List<Integer> firstact = new ArrayList<Integer>(act);

            for (int j = 0; j < N; j++) {
                buf.act(act , samurAINumber);
                act = getRandomAct();
            }

            int util = getUtility(buf);

            if(util > maxutil){
                maxutil = util;
                ret = firstact;
            }
        }

        //System.out.println("util " + maxutil);


        return ret;
    }

    @Override
    public String getName() {
        return "RandomN="+N+"Greedy";
    }

    public int getN() {
        return N;
    }
}
