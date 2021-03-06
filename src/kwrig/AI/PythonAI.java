package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Score;
import kwrig.python.PythonBridge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/22.
 */
public class PythonAI extends AI {

    public static boolean initFlag = true;

    public static PythonBridge pythonBridge = new PythonBridge();

    public int onlyNumber = 0;

    public PythonAI() {

        if (initFlag) {
            try {
                pythonBridge.init(true , 11,7);
            } catch (IOException e) {
                e.printStackTrace();

                System.err.println("initError");
                System.exit(1);

            }
            initFlag = false;
        }
    }

    boolean startFlag = true;

    double oldScore = 0;


    @Override
    public List<Integer> action(Field field, Field ura_field, Score score) {

        int actionPoint = 7;
        List<Integer> ret = new ArrayList<>();

        try {
            int action=0;
            if (startFlag) {
                List<Integer> fieldData = field.makeFieldDataList(samurAINumber, actionPoint, field);
                action = pythonBridge.start(fieldData);
                startFlag = false;
            }else {

                List<Integer> fieldData = field.makeFieldDataList(samurAINumber, actionPoint, field);

                double value = score.playerPoint[samurAINumber] + score.kill[samurAINumber]*10 - score.death[samurAINumber]*10;
                value/=300.0;

                action = pythonBridge.next(fieldData , value - oldScore);
                oldScore = value;
                startFlag = false;
            }


            actionPoint -= General.COST[action];
            ret.add(action);



            while (actionPoint > 0) {
                Field simField = new Field(field);
                simField.act(ret , samurAINumber);
                List<Integer> fieldData = field.makeFieldDataList(samurAINumber, actionPoint, field);
                action = pythonBridge.next(fieldData , 0);
                actionPoint -= General.COST[action];
                ret.add(action);
                System.err.println("actcheck " + action + "  " + actionPoint);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        ret.add(0);

        return ret;


    }

    @Override
    public String getName() {
        return "pythonAI";
    }

    @Override
    public void EndCall(Score score) {

        try {
            pythonBridge.end(score.getPoint(samurAINumber) / 300.0 - oldScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void fin(){
        try {
            pythonBridge.fin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void laod(String name){

        PythonBridge pb = new PythonBridge();
        try {
            pb.init(true , 11,7);
            pb.load(name);
            pythonBridge = pb;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static public void frozen(){
        try {
            System.err.println("Frozen");
            pythonBridge.frozen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int onlyCheck() {
        return onlyNumber;
    }
}
