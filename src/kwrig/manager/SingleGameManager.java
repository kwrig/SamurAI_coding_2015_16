package kwrig.manager;

import kwrig.AI.AI;
import kwrig.AI.PythonAI;
import kwrig.AI.RandomGreedyAI;
import kwrig.Field;
import kwrig.General;
import kwrig.Player;
import kwrig.Score;
import kwrig.simulator.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kwrig on 2016/01/03.
 */
public class SingleGameManager implements Runnable{



    Field field = new Field();

    List<AI> aiList = new ArrayList<AI>();

    Score score = null;

    public void setField(Field field) {
        this.field = field;
    }

    public void setAiList(List<AI> aiList) {
        this.aiList = aiList;
    }

    public void run(){


        Collections.shuffle(aiList);

        for (int i = 0; i < 6; i++) {

            Player player = new Player();
            player.homePosition = General.getPosition(General.FIRST_PLACE[i][0] ,General.FIRST_PLACE[i][1] );
            player.position = player.homePosition;
            player.samuraiNumber = i;

            field.players[i] = player;
        }


        System.out.println("GameStart");
        for (int i = 0; i < 6; i++) {

            System.out.print(aiList.get(i).getAINumber() + " ");
            if(i==2){
                System.out.print(" VS ");
            }

        }
        System.out.println();


        field.init();
        Game game = new Game(field , aiList);

        game.start();

        score = game.getScore();

        for (int i = 0; i < 6; i++) {

            score.AINumber[i] = aiList.get(i).getAINumber();

        }
        return;
    }


    public Score getScore() {
        return score;
    }

    public static void main(String[] args) {

        SingleGameManager manager = new SingleGameManager();
        manager.testRun();

    }


    void testRun(){


        List<AI> aiList = new ArrayList<AI>();

        Field field = new Field();

        AI ai = new PythonAI();

        aiList.add(ai);
        Player player = new Player();
        player.homePosition = General.getPosition(General.FIRST_PLACE[0][0] ,General.FIRST_PLACE[0][1] );
        player.position = player.homePosition;
        player.samuraiNumber = 0;

        field.players[0] = player;


        for (int i = 1; i < 6; i++) {

            ai = new RandomGreedyAI();
            aiList.add(ai);
            player = new Player();
            player.homePosition = General.getPosition(General.FIRST_PLACE[i][0] ,General.FIRST_PLACE[i][1] );
            player.position = player.homePosition;
            player.samuraiNumber = i;

            field.players[i] = player;
        }

        field.init();
        Game game = new Game(field , aiList);

        game.start();

        PythonAI.fin();



    }


}
