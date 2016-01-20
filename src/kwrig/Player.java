package kwrig;

/**
 * Created by kwrig on 2016/01/02.
 */
public class Player {

    public int samuraiNumber = 0;

    public int position = 0;

//    public Field field;

    public int homePosition = 0;

    public boolean isHide = false;

    public int health = 0;

    public int kill=0;
    public int death = 0;


    public Player() {
    }

    Player(Player player) {
        copy(player);

    }

    void copy(Player player) {
        samuraiNumber = player.samuraiNumber;
        position = player.position;
        homePosition = player.homePosition;
        health = player.health;
        isHide = player.isHide;

        kill = player.kill;
        death = player.death;

    }


    public boolean move(int dist , Field field) {

        int x = General.getXY(position)[0];
        int y = General.getXY(position)[1];

        x = x + General.MOVE_X[dist];
        y = y + General.MOVE_Y[dist];

        if (x < 0 || General.width <= x) {
            return false;
        }
        if (y < 0 || General.height <= y) {
            return false;
        }

        if(isHide){

            if(field.field[General.getPosition(x,y)]/3 != samuraiNumber/3  ){
                return false;
            }

        }

        for(Player player : field.players){

            if(player.samuraiNumber == samuraiNumber){
                continue;
            }
            if(player.position == General.getPosition(x,y)){

                if(player.isHide==false && isHide==false) {
                    return false;
                }


            }


        }


        position = General.getPosition(x,y);
        return true;
    }

    public void SetData(String str) {

        String[] splits = str.split(" ");

        if (Integer.parseInt(splits[0]) == -1) {
            return;
        }

        position = General.getPosition(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]));

    }


}
