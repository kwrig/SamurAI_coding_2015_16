package kwrig.AI;

import kwrig.Field;
import kwrig.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/03.
 */
public abstract class AI {

    int AINumber = 0;

    public int samurAINumber = 0;


    public List<Integer> action(Field field){
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



}
