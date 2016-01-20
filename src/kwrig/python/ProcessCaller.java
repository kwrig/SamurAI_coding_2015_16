package kwrig.python;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by kwrig on 15/08/02.
 */
public class ProcessCaller implements Runnable {

    boolean endFlag = false;

    BufferedWriter writer = null;

    InputStreamThread it=null;
    InputStreamThread et=null;

    @Override
    public void run() {

        ProcessBuilder pb = new ProcessBuilder("python", "dqn_agent_nature.py");



        try {
            Process process = pb.start();

            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            it = new InputStreamThread(process.getInputStream(),false);
            et = new InputStreamThread(process.getErrorStream(),true);
            it.start();
            et.start();

            System.out.println("proces Start");

            process.waitFor();

            //InputStreamのスレッド終了待ち
            it.join();
            et.join();
            it = null;
            et = null;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endFlag = true;


    }

    public boolean getEndFlag() {
        return endFlag;
    }

    public InputStreamThread getStdInputStream(){
        return it;
    }
    public InputStreamThread getSerrInputStream(){
        return et;
    }

    public BufferedWriter getBufferdWriter(){
        return writer;
    }

    public int getNextAction(){


        while (true) {

            String str = it.getNextString();



            if(str==null){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            String[] strs = str.split(" ");
            if(strs[0].equals("action")){
               // System.err.println(str);
                return Integer.parseInt(strs[1]);
            }
        }



    }


}
