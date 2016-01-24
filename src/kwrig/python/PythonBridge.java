package kwrig.python;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by kwrig on 2016/01/18.
 */
public class PythonBridge {


    ProcessCaller processCaller = new ProcessCaller();

    Thread thread = null;

    public void init(boolean isGPU , int actionNumber , int fieldNum) throws IOException {
        thread = new Thread(processCaller);
        thread.start();

        try {
            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedWriter writer= processCaller.getBufferdWriter();

        if(isGPU) {
            writer.write("gpu");
        }else {
            writer.write("cpu");
        }
        writer.newLine();
        writer.flush();

        writer.write(Integer.toString(actionNumber));
        writer.newLine();

        writer.write(Integer.toString(fieldNum));
        writer.newLine();
        writer.flush();



    }

    public void save(String name) throws IOException {

        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("save");
        writer.newLine();
        writer.flush();

        writer.write(name + "_model");
        writer.newLine();
        writer.flush();

        writer.write(name + "_load");
        writer.newLine();
        writer.flush();


    }

    public void load(String name) throws IOException {

        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("load");
        writer.newLine();
        writer.flush();

        writer.write(name + "_model");
        writer.newLine();
        writer.flush();

        writer.write(name + "_load");
        writer.newLine();
        writer.flush();


    }



    public int start(List<Integer> data) throws IOException {

        processCaller.getSerrInputStream().clear();
        processCaller.getStdInputStream().clear();


        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("start");
        writer.newLine();
        writer.flush();

        writer.write(parseStringFromList(data));
        writer.newLine();
        writer.flush();

        int move = processCaller.getNextAction();
        return move;
    }

    public int next(List<Integer> data , double reward) throws IOException {
        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("next");
        writer.newLine();
        writer.flush();

        writer.write(parseStringFromList(data));
        writer.newLine();
        writer.flush();

        writer.write(Double.toString(reward));
        writer.newLine();
        writer.flush();
        int move = processCaller.getNextAction();
        return move;
    }

    public void end(double reward) throws IOException {

        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("end");
        writer.newLine();
        writer.flush();
        writer.write(Double.toString(reward));
        writer.newLine();
        writer.flush();
        return;
    }


    public void fin() throws IOException {
        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("fin");
        writer.newLine();
        writer.flush();
        return;
    }

    public void frozen() throws IOException {
        BufferedWriter writer= processCaller.getBufferdWriter();
        writer.write("frozen");
        writer.newLine();
        writer.flush();
        return;

    }

    private String parseStringFromList(List<Integer> list){

        String ret= "";

        for(Integer i:list){
            ret += i +" ";
        }

        return ret;

    }



}
