package kwrig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kwrig on 2016/01/02.
 */
public class Field {


    String crlf = System.getProperty("line.separator");

    public static int maxTurn = 192;
    public static int width = 15;
    public static int hight = 15;

    public static int healthTime = 20;

    public Score score = new Score();

    public int turn = 0;


    public Player[] players = new Player[6];
    public int[] field;

    public void init() {

        field = new int[width * hight];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < hight; y++) {
                setField(8, x, y);
            }
        }

        for (int i = 0; i < 6; i++) {
            Player player = players[i];
            field[player.position] = i;
        }

    }

    public void nextTurn() {
        turn++;

        for (Player player : players) {
            player.health = Math.max(0, player.health - 1);
        }

        score.calcScore(this);

    }

    public Field() {

    }

    public Field(Field f) {
        copy(f);
    }

    public void copy(Field f) {

        turn = f.turn;

        for (int i = 0; i < f.players.length; i++) {
            Player player = new Player(f.players[i]);
            players[i] = player;
        }

        field = Arrays.copyOf(f.field, f.field.length);

        score = new Score(f.score);

    }

    public Field makeOneSideField(int samurAINumber){


        int team = samurAINumber/3;

        boolean[] sees = new boolean[width*hight];

        Field ret = new Field(this);

        for (int i = 0; i < 3; i++) {

            Player player = players[i + team*3];
            int[] XY = General.getXY(player.position);


            //視野範囲の計算
            for (int x = -5; x < 6; x++) {
                for (int y = -5; y < 6; y++) {

                    if(Math.abs(x) + Math.abs(y) >5){
                        continue;
                    }
                    if(!General.isRangeY(XY[1]+y)){
                        continue;
                    }
                    if(!General.isRangeX(XY[0]+x)){
                        continue;
                    }
                    sees[General.getPosition( XY[0]+x , XY[1]+y )] = true;
                }
            }
        }

        for (int i = 0; i < width*hight; i++) {
            if(sees[i]==false){
                ret.field[i] = 9;
            }
        }

        for (int i = 0; i < 6; i++) {
            if(i/3 == team){
                continue;
            }

            Player player = ret.players[i];

            if(sees[player.position] == false || player.isHide==true){
                player.position = player.homePosition;
                player.isHide = true;
            }
        }
        return ret;

    }



    public void setField(int num, int x, int y) {

        field[General.getPosition(x, y)] = num;

    }

    public void act(List<Integer> action, int actPlayer){

        int costSum = 0;

        if(players[actPlayer].health > 0){
            return;
        }


        if (action==null){
            return;
        }

        for (Integer act :action){

            costSum += General.COST[act];
            if(costSum > 7){
                return;
            }

            if(act(act,actPlayer) == false){
                return;
            }
        }
    }


    private boolean act(int actionNumber, int actionPlayer) {

        Player player = players[actionPlayer];


        switch (actionNumber) {
            case 1:
            case 2:
            case 3:
            case 4:

                if(player.isHide == true){
                    return false;
                }
                attack(actionNumber - 1, actionPlayer);
                break;



            case 5:
            case 6:
            case 7:
            case 8:
                if (!players[actionPlayer].move(actionNumber - 5 ,this)) {
                    return false;
                }
                break;

            case 9:


                if (player.isHide == true || field[player.position] / 3 != actionPlayer / 3) {
                    return false;
                }
                players[actionPlayer].isHide = true;
                break;
            case 10:

                if (player.isHide == false) {
                    return false;
                }
                for (int i = 0; i < 6; i++) {

                    if (i == actionPlayer) {
                        continue;
                    }
                    if (players[i].position == player.position) {
                        return false;
                    }
                    player.isHide = false;

                }
                break;
            default:
                return false;

        }

        return true;
    }

    private boolean attack(int dist, int actionPlayer) {

        int[][] attackRange = General.getAttackRange(actionPlayer, dist);


        Player player = players[actionPlayer];
        int[] pos = General.getXY(player.position);
        for (int[] p : attackRange) {
            attackOne(pos[0] + p[0], pos[1] + p[1], actionPlayer);
        }

        //拠点の色は変わらない
        for(Player pl : players){
            field[pl.homePosition] = pl.samuraiNumber;
        }

        return true;

    }


    private boolean attackOne(int x, int y, int num) {

        if (General.isRangeX(x) && General.isRangeY(y)) {
            field[General.getPosition(x, y)] = num;

            for(Player player : players){

                //拠点は死なない
                if(player.position == player.homePosition){
                    continue;
                }

                //死亡判定
                if( General.getPosition(x,y) == player.position && num/3 != player.samuraiNumber/3 ){
                    player.position = player.homePosition;
                    player.health = healthTime;
                    score.death[player.samuraiNumber]++;
                    score.kill[num]++;
                }

            }


            return true;
        }
        return false;
    }

    public List<Player> getPositionPlayer(int position){

        List<Player> ret = new ArrayList<Player>();

        for (Player player : players){
            if(player.position == position){
                ret.add(player);
            }
        }
        return ret;

    }


    @Override
    public String toString() {
        String ret = "";

        ret += "turn :" + turn + "  player " + General.ORDER[turn%General.ORDER.length] + crlf;

        ret += "score " + score.teamPoint[0] + " : " + score.teamPoint[1] + crlf;

        ret+= "playerScore ";
        for (int i = 0; i < 6; i++) {

            ret += score.playerPoint[i] + " ";
        }
        ret += crlf;


        for (int y = 0; y < hight; y++) {
            for (int x = 0; x < width; x++) {

                ret += field[General.getPosition(x, y)];

                List<Player> list = getPositionPlayer(General.getPosition(x,y));
                if(list.size() == 1){

                    if(list.get(0).isHide){
                        ret += "*";
                    }else {

                        ret += list.get(0).samuraiNumber;
                    }
                }else if(list.size() > 1){
                    ret += "*";
                }else{
                    ret += " ";
                }


            }
            ret += crlf;
        }
        ret += "player " + crlf;
        for (int i = 0; i < 6; i++) {

            int[] pos = General.getXY(players[i].position);
            ret += pos[0] +" "+pos[1] + " H" + players[i].health  + " K" + score.kill[i] + " D" + score.death[i];

            if(players[i].isHide){
                ret+= " hide";
            }

            ret+=crlf;
        }


        return ret;
    }




}
