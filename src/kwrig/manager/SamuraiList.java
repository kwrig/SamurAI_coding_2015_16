package kwrig.manager;

import kwrig.AI.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/07.
 */
public class SamuraiList extends ArrayList<AI> {


    public static List<List<Double>> WeightsList=null;





    SamuraiList() {

        super();

        if(WeightsList == null) {

            init();
        }else{
            init2();
        }
        int count = 0;
        for (AI ai : this) {
            ai.setAINumber(count);
            count++;
        }
    }


    private void init2(){


        for(List<Double> weights : WeightsList){
            NeoEnemyEstimate ai = new NeoEnemyEstimate(2);
            ai.Weights = weights;
            add(ai);
        }
        //1割程度は普通のAIも加えておく
        for(int i = 0; i < WeightsList.size()/20+1; i++) {
            add(new EnemyEstimateAndAttack(2,true));
        }
        for(int i = 0; i < WeightsList.size()/20+1; i++) {
            add(new EnemyEstimateAndAttack(2,true,3));
        }



        while (size() % 6 != 0) {
            add(new EnemyEstimateAndAttack(2,true));
        }




    }


    //以下AIの登録

    private void init() {

        //ここから登録



        add(new EnemyDodgeAI(2));
        add(new EnemyDodgeAI(2));

        add(new EnemyEstimate(2));
        add(new EnemyEstimate(2));

        add(new EnemyDodgeAI(2,true));
        add(new EnemyDodgeAI(2,true));

        add(new EnemyEstimate(2,true));
        add(new EnemyEstimate(2,true));

        add(new EnemyEstimateAndAttack(2));
        add(new EnemyEstimateAndAttack(2));
        add(new EnemyEstimateAndAttack(2,true));
        add(new EnemyEstimateAndAttack(2,true));

        add(new EnemyEstimateAndAttack(2,true,3));
        add(new EnemyEstimateAndAttack(2,true,3));


        add(new NeoEnemyEstimate(2,0,false));
        add(new NeoEnemyEstimate(2,3,false));
        add(new NeoEnemyEstimate(2,4,false));
        add(new NeoEnemyEstimate(2,5,false));
        add(new NeoEnemyEstimate(2,6,false));
        add(new NeoEnemyEstimate(2,7,false));

        add(new NeoEnemyEstimate(2,0,true));
        add(new NeoEnemyEstimate(2,3,true));
        add(new NeoEnemyEstimate(2,4,true));
        add(new NeoEnemyEstimate(2,5,true));
        add(new NeoEnemyEstimate(2,6,true));
        add(new NeoEnemyEstimate(2,7,true));
/*

       add(new NeoEnemyEstimate(2,0));
        //add(new EnemyEstimate(2,true));
        add(new EnemyDodgeAI(2));
        add(new EnemyDodgeAI(2));
        add(new EnemyDodgeAI(2));
        add(new EnemyDodgeAI(2));

*/

        //登録ここまで

        while (size() % 6 != 0) {
            add(new RandomAI());
        }


    }





}
