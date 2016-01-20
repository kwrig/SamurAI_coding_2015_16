package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kwrig on 2016/01/03.
 */
public class SimpleAI {

    BufferedReader stdReader =
            new BufferedReader(new InputStreamReader(System.in));

    Field field = new Field();

    void initRead() throws IOException {

        System.out.println("# init read");

        String string = readNext();

        String[] splits = string.split(" ");
        field.maxTurn = Integer.parseInt(splits[0]);
       // field.mySamuraiNumber = Integer.parseInt(splits[1]) * 3 + Integer.parseInt(splits[2]);
        field.width = Integer.parseInt(splits[3]);
        field.hight = Integer.parseInt(splits[4]);
        field.healthTime = Integer.parseInt(splits[5]);

        General.width = field.width;
        General.height = field.hight;


        for (int i = 0; i < 6; i++) {

            string = readNext();
            splits = string.split(" ");

            Player player = new Player();

            int pos = General.getPosition( Integer.parseInt(splits[0]) , Integer.parseInt(splits[1]) );
            player.position = pos;
            player.homePosition = pos;
            player.samuraiNumber = i;


            field.players[i] = player;
        }

        for (int i = 0; i < 6; i++) {
            readNext();
        }

        System.out.println("# end init");

    }

    String readNext() throws IOException {

        String string = stdReader.readLine();


        while (string==null || string.charAt(0) == '#'){
            string = stdReader.readLine();
        }

        return string;




    }


    void readTurnInformation() throws IOException {

        String string = readNext();


        Field nextField = new Field(field);

        nextField.turn = Integer.parseInt(string);

        string = readNext();

       // nextField.health = Integer.parseInt(string);


        for (int i = 0; i < 6; i++) {
            string = readNext();
            nextField.players[i].SetData(string);
        }


        for (int i = 0; i < nextField.hight; i++) {

            string = readNext();

            String[] splits = string.split(" ");

            for (int j = 0; j < nextField.width; j++) {
                nextField.setField(Integer.parseInt(splits[j]) , i,j);
            }
        }

    }


    void action() throws IOException {

        readTurnInformation();

        System.out.println("0");
        System.out.flush();


    }

    void run() throws IOException {

        initRead();

        System.out.println("# print0");

        System.out.println("0");


        int turnCount = 0;

        while (true){

            action();



            turnCount++;
            if(turnCount > 10000){
                break;
            }
        }






    }

    public static void main(String[] args) throws IOException {

        SimpleAI main = new SimpleAI();

        main.run();


    }










}
