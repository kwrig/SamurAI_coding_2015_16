package kwrig.manager;

import java.util.Comparator;

/**
 * Created by kwrig on 2016/01/08.
 */
public class AIResult implements Comparator<AIResult>{

    int point = 0;
    int kill = 0;
    int death = 0;

    int AInumber = 0;

    public void addResult(AIResult result){

        addPoint(result.getPoint());
        addKill(result.getKill());
        addDeath(result.getDeath());

    }


    public void addPoint(int point){
        this.point += point;
    }

    public void addKill(int k){
        kill+=k;
    }
    public void addDeath(int d){
        death += d;
    }


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public int getAInumber() {
        return AInumber;
    }

    public void setAInumber(int AInumber) {
        this.AInumber = AInumber;
    }

    @Override
    public int compare(AIResult o1, AIResult o2) {
        return o1.point - o2.point;
    }


    @Override
    public String toString() {
        return "No." + AInumber + " : point " + point + " : kill " + kill + " : death " + death;
    }
}
