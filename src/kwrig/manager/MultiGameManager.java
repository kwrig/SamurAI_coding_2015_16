package kwrig.manager;

import kwrig.AI.AI;
import kwrig.Field;
import kwrig.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kwrig on 2016/01/07.
 */
public class MultiGameManager {

    int section_total = 500;


    TournamentResult result = new TournamentResult();



    
    void run_OneSection() throws InterruptedException {

        TournamentResult sortResult = new TournamentResult(result);

        Collections.shuffle(sortResult);
        Collections.sort(sortResult , new AIResult());
        Collections.reverse(sortResult);

        List<AI> aiList = new ArrayList<AI>();

        SamuraiList samurailist = new SamuraiList();


        List<Thread> threadList = new ArrayList<Thread>();
        List<SingleGameManager> singleGameManagerList = new ArrayList<SingleGameManager>();


        for(AIResult aiResult : sortResult){

            aiList.add(samurailist.get(aiResult.getAInumber()));

            if(aiList.size() == 6){

                SingleGameManager singleGameManager = new SingleGameManager();
                singleGameManager.aiList = new ArrayList<AI>(aiList);
                singleGameManager.field = new Field();

                singleGameManagerList.add(singleGameManager);
                Thread thread = new Thread(singleGameManager);
                thread.start();
                threadList.add(thread);




                aiList.clear();
            }
        }

        for (Thread thread:threadList){
            thread.join();
        }

        for (SingleGameManager manager:singleGameManagerList){

            Score score = manager.getScore();

            List<AIResult> results = score.getAIResults();
            System.out.println(results.toString());

            for (AIResult airesult:results){
                result.addAIResult(airesult);
            }
        }

        System.out.println(result.toString());

    }


    void run() throws InterruptedException {

        System.out.println(result.toString());


        for (int i = 0; i < section_total; i++) {
            System.out.println(i +" th round");
            run_OneSection();


        }
        

    }

    public static void main(String[] args) throws InterruptedException {

        MultiGameManager manager = new MultiGameManager();
        manager.run();

    }


}
