package kwrig.manager;

import kwrig.AI.PythonAI;
import kwrig.io.FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by kwrig on 2016/01/26.
 */
public class PythonTestManager {


    int loop = 300;
    
    void run() throws IOException {
        BufferedReader r =
                new BufferedReader(new InputStreamReader(System.in), 1);
        // データ入力の準備
        System.out.print("どこまで測る?");
        System.out.flush();             // 強制出力
        String s = r.readLine();        // 文字列の入力
        int inputNumber = Integer.parseInt(s);    // 整数に変換

        FileIO io = new FileIO();

        for (int counter = 0; counter <inputNumber ; counter++) {

            PythonLearnGameManager manager = new PythonLearnGameManager();
            for (int i = 0; i < 3; i++) {
                manager.pythonSamuraiNumber = i;
                PythonAI.laod("battle" + (counter*1000) + "samurai" + i);
                PythonAI.frozen();
                for (int l = 0; l < loop; l++) {
                    manager.singleGame();
                }
                PythonAI.fin();
            }

            io.output("point.csv" , stringFromList(manager.playerPointList));
            io.output("kill.csv" , stringFromList(manager.playerkillList));
            io.output("death.csv" , stringFromList(manager.playerDeathList));
            io.output("win.csv" , stringFromList(manager.playerWinList));
        }

    }


    String stringFromList(List<Integer> list){

        String ret = "";

        for(Integer i:list){
            ret += i +",";
        }
        return ret;

    }

    public static void main(String[] args) throws IOException {

        PythonTestManager manager = new PythonTestManager();
        manager.run();

    }



}
