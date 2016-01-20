package kwrig.manager;

import kwrig.AI.*;

import java.util.ArrayList;

/**
 * Created by kwrig on 2016/01/07.
 */
public class SamuraiList extends ArrayList<AI> {


    SamuraiList(){

        super();
        init();
    }



    //以下AIの登録

    private void init(){

        //ここから登録

        add(new RandomGreedyAI());


        add(new RandomGreedyAI());
        add(new RandomGreedyAI());
        add(new RandomGreedyAI());

        add(new RandomNGreedy(2));
        add(new RandomNGreedy(2));
        add(new RandomNGreedy(2));
        add(new RandomNGreedy(3));
        add(new RandomNGreedy(3));
        add(new RandomNGreedy(3));

        add(new NeoRandomNGreedy(1));
        add(new NeoRandomNGreedy(1));
        add(new NeoRandomNGreedy(1));
        add(new NeoRandomNGreedy(2));
        add(new NeoRandomNGreedy(2));
        add(new NeoRandomNGreedy(2));
        add(new NeoRandomNGreedy(3));
        add(new NeoRandomNGreedy(3));
        add(new NeoRandomNGreedy(3));


        //登録ここまで

        while (size()%6 != 0){
            add(new RandomAI());
        }

        int count = 0;
        for(AI ai:this){
            ai.setAINumber(count);
            count++;
        }
    }


}
