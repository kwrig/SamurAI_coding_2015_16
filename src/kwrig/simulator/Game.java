package kwrig.simulator;

import kwrig.AI.AI;
import kwrig.Field;
import kwrig.General;
import kwrig.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/03.
 */
public class Game {

    /*
    フィールドとAIを渡して初期化

     */

    List<Field> fields = new ArrayList<Field>();

    List<AI> aiList = new ArrayList<AI>();


    Score score=null;

    int orderSize = 12;

    public Game(Field field , List<AI> ais){
        fields.add(field);

        field.init();

        maxTurn = field.maxTurn;

        fields.add(new Field(field));
        aiList = ais;

        for (int i = 0; i < 6; i++) {

            aiList.get(i).init(field , field.players[i]);

        }

    }


    int maxTurn;

    public void start(){

        for (int i = 0; i < maxTurn; i+=orderSize) {

            for (int j = 0; j < orderSize; j++) {

                Field nextField = new Field(fields.get(fields.size()-1));



                List<Integer> action = aiList.get(General.ORDER[j]).action(nextField.makeOneSideField(General.ORDER[j]));
                nextField.act(action , General.ORDER[j]);

                nextField.nextTurn();

                /*
                System.out.println(action.toString());

                System.out.println("field");
                System.out.println(nextField.toString());

                System.out.println("user");
                System.out.println(nextField.makeOneSideField(General.ORDER[j]).toString());

                   */

                fields.add(nextField);

            }
        }
        score = fields.get(fields.size()-1).score;
        score.calcScore(fields.get(fields.size()-1));


    }


    public Score getScore() {
        return score;
    }
}
