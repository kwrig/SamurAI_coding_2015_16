package kwrig.manager;

import java.util.Comparator;

/**
 * Created by kwrig on 2016/01/08.
 */
public class AIResult implements Comparator<AIResult>{

    double point = 0;
    int kill = 0;
    int death = 0;

    int AInumber = 0;

    double attuniationRate = 0.98;

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
        return (int)point;
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

    public void attenuation(){
        point += attuniationRate;
    }

    @Override
    public int compare(AIResult o1, AIResult o2) {
        double d = o1.point - o2.point;
        if(d > 0){
            return 1;
        }
        if(d < 0){
            return -1;
        }
        return 0;
    }


    @Override
    public String toString() {
        return "No." + AInumber + " : point " + point + " : kill " + kill + " : death " + death;
    }
}
