package kwrig.manager;

import kwrig.AI.AI;
import kwrig.AI.NeoRandomNGreedy;
import kwrig.AI.PythonAI;
import kwrig.Field;
import kwrig.General;
import kwrig.Player;
import kwrig.Score;
import kwrig.simulator.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/24.
 */
public class PythonLearnGameManager {


    List<Integer> playerPointList = new ArrayList<>();
    List<Integer> playerkillList = new ArrayList<>();
    List<Integer> playerDeathList = new ArrayList<>();
    List<Integer> playerWinList = new ArrayList<>();


    int listNumber = 1000;
    int pythonSamuraiNumber = 0;


    void singleGame() {
        List<AI> aiList = new ArrayList<AI>();

        Field field = new Field();

        AI ai;
        Player player;
        for (int i = 0; i < 6; i++) {

            if (i == pythonSamuraiNumber) {
                ai = new PythonAI();

            } else {
                ai = new NeoRandomNGreedy(2);
            }
            aiList.add(ai);
            player = new Player();
            player.homePosition = General.getPosition(General.FIRST_PLACE[i][0], General.FIRST_PLACE[i][1]);
            player.position = player.homePosition;
            player.samuraiNumber = i;

            field.players[i] = player;
        }

        field.init();
        Game game = new Game(field , aiList);

        game.start();

        Score score = game.getScore();

        for (int i = 0; i < 6; i++) {
            aiList.get(i).EndCall(score);
        }

        playerPointList.add(score.getPoint(pythonSamuraiNumber));
        while (playerPointList.size() > listNumber){
            playerPointList.remove(0);
        }

        playerkillList.add(score.kill[pythonSamuraiNumber]);
        while (playerkillList.size() > listNumber){
            playerkillList.remove(0);
        }

        playerDeathList.add(score.death[pythonSamuraiNumber]);
        while (playerDeathList.size() > listNumber){
            playerDeathList.remove(0);
        }

        playerWinList.add( score.getPoint(pythonSamuraiNumber)/300);
        while (playerWinList.size() > listNumber){
            playerWinList.remove(0);
        }


        System.out.println("playerPoint = " + Sum(playerPointList));
        System.out.println("playerKill = " + Sum(playerkillList));
        System.out.println("playerDeath = " + Sum(playerDeathList));
        System.out.println("playerWin = " + Sum(playerWinList));

    }


    void run() throws IOException {
        BufferedReader r =
                new BufferedReader(new InputStreamReader(System.in), 1);
        // データ入力の準備
        System.out.print("PythonAIのSamuraiNumber入力");
        System.out.flush();             // 強制出力
        String s = r.readLine();        // 文字列の入力
        pythonSamuraiNumber = Integer.parseInt(s);    // 整数に変換


        for (int i = 0; i < 1000000; i++) {
            singleGame();

            System.out.println("round" + i + " end");

            if(i%1000 == 999){
                PythonAI.pythonBridge.save("battle" + (i+1) +"samurai" + pythonSamuraiNumber);
            }

        }

        PythonAI.fin();

    }

    public static void main(String[] args) throws IOException {

        PythonLearnGameManager manager = new PythonLearnGameManager();
        manager.run();

    }

    public static int Sum(List<Integer> list){

        int ret = 0;

        for(Integer integer : list){
            ret += integer;
        }

        return ret;

    }

}
