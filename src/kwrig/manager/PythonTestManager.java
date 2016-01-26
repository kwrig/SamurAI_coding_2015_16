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
        System.out.print("from to name?");
        System.out.flush();             // 強制出力
        String s = r.readLine();        // 文字列の入力
        String[] splits = s.split(" ");
        int from = Integer.parseInt(splits[0]);    // 整数に変換

        int to = Integer.parseInt(splits[1]);

        int name = Integer.parseInt(splits[2]);


        FileIO io = new FileIO();

        for (int counter = from; counter <=to ; counter++) {

            PythonLearnGameManager manager = new PythonLearnGameManager();
            for (int i = 0; i < 3; i++) {
                manager.pythonSamuraiNumber = i;
                PythonAI.laod("log/battle" + (counter*1000) + "samurai" + i);
                PythonAI.frozen();
                for (int l = 0; l < loop; l++) {
                    manager.singleGame();
                }
                PythonAI.fin();
            }

            io.output("point" + name +".csv" , stringFromList(manager.playerPointList));
            io.output("kill" + name + ".csv" , stringFromList(manager.playerkillList));
            io.output("death" + name + ".csv" , stringFromList(manager.playerDeathList));
            io.output("win" + name + ".csv" , stringFromList(manager.playerWinList));
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

        PythonAI.initFlag = false;

        PythonTestManager manager = new PythonTestManager();
        manager.run();

    }



}
