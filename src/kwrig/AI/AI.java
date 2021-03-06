package kwrig.AI;

import kwrig.Field;
import kwrig.General;
import kwrig.Player;
import kwrig.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/03.
 */
public abstract class AI {

    int AINumber = 0;

    public int samurAINumber = 0;


    public List<Integer> action(Field field , Field ura_field , Score score){
        return new ArrayList<Integer>();
    }


    public void init(Field field , Player player){

        samurAINumber = player.samuraiNumber;

    }

    public void setAINumber(int AINumber) {
        this.AINumber = AINumber;
    }

    public int getAINumber() {
        return AINumber;
    }

    public abstract String getName();

    public abstract void EndCall(Score score);


    public int onlyCheck(){
        return -1;
    }

    public AI(){
        General general = new General();
    }

}
