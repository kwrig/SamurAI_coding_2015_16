package kwrig.manager;

import kwrig.General;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kwrig on 2016/01/08.
 */
public class TournamentResult extends ArrayList<AIResult>{

    public TournamentResult(TournamentResult result){
        super(result);
    }

    public TournamentResult(){
        SamuraiList list = new SamuraiList();

        for (int i = 0; i < list.size(); i++) {

            AIResult result = new AIResult();
            result.setAInumber(i);
            add(result);
        }
    }

    public void addAIResult(AIResult result){

        get(result.getAInumber()).addResult(result);

    }



    @Override
    public String toString() {

        String nn = General.crlf;
        String ret = "TournamentResult" + nn;



        SamuraiList samuraiList = new SamuraiList();

        TournamentResult result = new TournamentResult(this);
        Collections.sort(result , new AIResult());
        Collections.reverse(result);

        int rank = 1;

        for (AIResult aiResult : result){

            int aiNumber = aiResult.getAInumber();

            ret += "rank" + rank + ":" +samuraiList.get(aiNumber).getName() + aiNumber + " : P " + aiResult.getPoint() + " : K " +
                    aiResult.getKill() + " : D " + aiResult.getDeath();

            ret += nn;
            rank++;
        }


        return ret;

    }
}


