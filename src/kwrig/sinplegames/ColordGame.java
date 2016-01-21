package kwrig.sinplegames;

import kwrig.python.PythonBridge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/18.
 */
public class ColordGame {


    int size = 17;

    PythonBridge pythonBridge = new PythonBridge();

    int[] MOVE_X = {1,0,-1,0};
    int[] MOVE_Y = {0,1,0,-1};

    int maxTurn = 100;
    int turn = 0;

    int a_x;
    int a_y;
    int b_x;
    int b_y;

    double reward_Max = 30.0;

    void game_init(){

        a_x = rand(size);
        a_y = rand(size);
        b_x = rand(size);
        b_y = rand(size);
        turn = 0;

    }


    double action(int n){

        int dist = 0;
        dist += Math.abs(a_x - b_x) + Math.abs(a_x - b_y);

        switch (n){
            case 0:
            case 1:
            case 2:
            case 3:

                a_x = Math.max(0 , Math.min(size-1 , a_x + MOVE_X[n]));
                a_y = Math.max(0 , Math.min(size-1 , a_y + MOVE_Y[n]));
                b_x = Math.max(0 , Math.min(size-1 , b_x + MOVE_X[(n+1)%4]));
                b_y = Math.max(0 , Math.min(size-1 , b_y + MOVE_Y[(n+1)%4]));
                break;

            case 4:
            case 5:
            case 6:
            case 7:
                a_x = Math.max(0 , Math.min(size-1 , a_x + MOVE_X[n-4]));
                a_y = Math.max(0 , Math.min(size-1 , a_y + MOVE_Y[n-4]));
                b_x = Math.max(0 , Math.min(size-1 , b_x + MOVE_X[(n-1)%4]));
                b_y = Math.max(0 , Math.min(size-1 , b_y + MOVE_Y[(n-1)%4]));
                break;

            case 8:
            case 9:
            case 10:
            case 11:
                a_x = Math.max(0 , Math.min(size-1 , a_x + MOVE_X[n-8]));
                a_y = Math.max(0 , Math.min(size-1 , a_y + MOVE_Y[n-8]));

            default:
                break;
        }



        int newdist = Math.abs(a_x - b_x) + Math.abs(a_x - b_y);
        double ret = dist - newdist;
        if(a_x==b_x && a_y==b_y){
            ret = 30;
        }


        return ret;
    }

    List<Integer> toList(){

        List<Integer> ret = new ArrayList<>();


        for (int i = 0; i < size*size; i++) {

            if(a_x==b_x && a_y==b_y){
                ret.add(128);
            }else if(i == a_x + a_y*size){
                ret.add(0);
            }else if(i== b_x + b_y*size){
                ret.add(252);
            }else {
                ret.add(128);
            }
        }

        return ret;



    }



    void run() throws IOException {

        pythonBridge.init(false);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("gameStart");

        int fullTurnCount = 0;
        int fullTurnMax = 1000000;



        for (int i = 0; i < 100000; i++) {

            int action = 0;

            game_init();

            action = pythonBridge.start(toList());
            double reward = action(action);

            for (int j = 0; j < maxTurn; j++) {

                fullTurnCount++;

                System.out.println("round " +i +" action" + action + " reward = " +reward +" playerX " + a_x +"," + a_y + " Y " + b_x + "," + b_y);
                action = pythonBridge.next(toList() ,reward/reward_Max);
                reward = action(action);
                gamePrint();
                if(reward > 9){
                    break;
                }
            }
            pythonBridge.end(reward/reward_Max);
            if(fullTurnCount > fullTurnMax){
                break;
            }
        }

        pythonBridge.frozen();

        for (int i = 0; i < 10; i++) {

            int action = 0;

            game_init();

            action = pythonBridge.start(toList());
            double reward = action(action);

            for (int j = 0; j < maxTurn; j++) {

                System.out.println("round " +i +"action" + action + " reward = " +reward +" playerX " + a_x +"," + a_y + " Y " + b_x + "," + b_y);
                action = pythonBridge.next(toList() ,reward);
                gamePrint();
                reward = action(action);
                if(reward > 5){
                    break;
                }
            }
            pythonBridge.end(reward);
        }



        pythonBridge.fin();
    }


    void gamePrint(){


        for (int x = -1; x < 18; x++) {
            String line="";
            for (int y = -1; y < 18; y++) {
                line += getChar(x,y);
            }
            System.out.println(line);
        }
    }
    String getChar(int x,int y){

        if(x==-1 || x == 17){
            return "-";
        }
        if(y==-1 || y == 17){
            return "|";
        }
        if(a_x==x && a_y==y){
            return "A";
        }
        if(b_x==x && b_y==y){
            return "B";
        }
        return ".";




    }


    int rand(int max){

        return (int)(Math.random()*max);

    }

    public static void main(String[] args) throws IOException {

        ColordGame main = new ColordGame();

        main.run();

    }






}
