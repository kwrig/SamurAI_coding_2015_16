package kwrig;

import kwrig.AI.AI;
import kwrig.AI.NeoEnemyEstimate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by kwrig on 2016/01/09.
 */
public class IOBridge {

    BufferedReader stdReader =
            new BufferedReader(new InputStreamReader(System.in));

    Field field = new Field();

   // AI ai = new EnemyDodgeAI(2);
    //AI ai = new EnemyEstimateAndAttack(2,true,3);
    AI ai = new NeoEnemyEstimate(2,3,false);

    General general = new General();

    int team = 0;

    void initRead() throws IOException {

        System.out.println("# init read");

        String string = readNext();

        String[] splits = string.split(" ");
        field.maxTurn = Integer.parseInt(splits[0]);

        team = Integer.parseInt(splits[1]);
        //int mySamuraiNumber = Integer.parseInt(splits[1]) * 3 + Integer.parseInt(splits[2]);
        int mySamuraiNumber = Integer.parseInt(splits[2]) + team*3;
        field.width = Integer.parseInt(splits[3]);
        field.hight = Integer.parseInt(splits[4]);
        field.healthTime = Integer.parseInt(splits[5]);

        General.width = field.width;
        General.height = field.hight;


        for (int i = 0; i < 6; i++) {

            int samurai = (i +team*3)%6;
            string = readNext();
            splits = string.split(" ");

            Player player = new Player();

            int pos = General.getPosition( Integer.parseInt(splits[0]) , Integer.parseInt(splits[1]) );
            player.position = pos;
            player.homePosition = pos;
            player.samuraiNumber = samurai;

            field.players[samurai] = player;
        }

        for (int i = 0; i < 6; i++) {
            readNext();
        }

        ai.init(field , field.players[mySamuraiNumber]);

        field.init();

        System.out.println("# end init");

    }

    String readNext() throws IOException {

        String string = stdReader.readLine();


        while (string==null || string.charAt(0) == '#'){
            string = stdReader.readLine();
        }

        return string;
    }


    public Field readTurnInfo() throws IOException {
        String[] res = this.read();

        Field nextField = new Field(field);


        if (res.length == 0){
            System.exit(-1);
        }

        nextField.turn = Integer.parseInt(res[0]);

        if (nextField.turn < 0){
            System.exit(-1);
        }

        res = this.read();
        int curePeriod = Integer.parseInt(res[0]);

        for (int i = 0; i < 6; ++i){
            res = this.read();

            int samurai = (i +team*3)%6;

            Player player = new Player(field.players[samurai]);
            int x = Integer.parseInt(res[0]);
            int y = Integer.parseInt(res[1]);

            if(General.isRangeX(x) && General.isRangeY(y)){
                player.position = General.getPosition(x,y);

            }else {
                player.position = player.homePosition;
                player.isHide = true;
            }
            int hidden = Integer.parseInt(res[2]);
            if(hidden==0){
                player.isHide = false;
            }else {
                player.isHide=true;
            }
            nextField.players[samurai] = player;
        }

        for (int i = 0; i < nextField.hight; i++) {

            String string = readNext();

            String[] splits = string.split(" ");

            int nnnn = 0;
            for (int j = 0; j < nextField.width; j++) {

                if(isNumber(splits[j+nnnn])) {

                    int ff = Integer.parseInt(splits[j+nnnn]);
                    if(ff < 6) {
                        ff = (Integer.parseInt(splits[j + nnnn]) + team * 3) % 6;
                    }
                    nextField.setField(ff, j, i);
                }else{
                    nnnn++;
                    j--;
                }
            }
        }

        return nextField;
    }



    void action() throws IOException {


        List<Integer> act = ai.action(field,null,null);

        String prints = "";

         boolean start = true;

        for(Integer i:act){

            if(start){
                start = false;
            }else{
                prints+=" ";
            }

            prints += i;
        }
       // System.err.println(prints);
        System.out.println(prints);



    }

    void run() throws IOException {

        initRead();


        System.out.println("# print0");

        System.out.println("0");


        int turnCount = 0;

        while (true){


            field = readTurnInfo();
          //  System.err.println(field.toString());

            action();



            turnCount++;
            if(turnCount > 10000){
                break;
            }
        }

    }

    public static void main(String[] args) throws IOException {

        IOBridge main =new IOBridge();

        main.run();


    }

    public String[] read(){
        String line = "";
        try{
            for (line = stdReader.readLine(); line.startsWith("#"); line = stdReader.readLine());
        } catch (Exception e) {
            e.getStackTrace();
            System.exit(-1);
        }
        return line.split("\\s");
    }


    public boolean isNumber(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException nfex) {
            return false;
        }
    }

}
