package kwrig.manager;

import kwrig.AI.DataStrage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kwrig on 2016/02/08.
 */
public class SearchGameManager {


    //上位生き残り数
    int selectPlayerNumber = 12;

    //近傍探索数
    int growthPlayerNumber = 5;


    List<GeneticPlayer> players = new ArrayList<>();




    void run() throws InterruptedException {

        players = new GeneticPlayer().getNearPlayer();

        for(int i = 0; i < 100000; i++) {

            SendSamuraiList(players);
            MultiGameManager manager = new MultiGameManager();
            TournamentResult result = manager.run();

            Collections.sort(result , new AIResult());
            Collections.reverse(result);

            List<GeneticPlayer> next = new ArrayList<>();

            write(i+"thRound");

            int bufc=0;
            boolean flag = true;
            for(int j = 0; j < selectPlayerNumber; j++) {
                int number = result.get(j+bufc).AInumber;
                if( number >= players.size()){
                    bufc++;
                    j--;
                    flag = false;
                    continue;
                }
                if(bufc+j >= players.size()){
                    break;
                }

                GeneticPlayer player = players.get(number);
                if(flag) {

                }
                write((j+bufc)+"th "+ player.toString());
                player.count++;
                next.add(player);
            }
            for(int j = 0; j < growthPlayerNumber; j++) {
                int number = result.get(j+bufc).AInumber;
                if( number >= players.size()){
                    bufc++;
                    j--;
                    continue;
                }
                if(bufc+j >= players.size()){
                    break;
                }
                GeneticPlayer player = players.get(number);
                next.addAll(player.getNearPlayer());
            }
            players = next;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        SearchGameManager main = new SearchGameManager();
        main.run();
    }



    void SendSamuraiList(List<GeneticPlayer> players){

        List<List<Double>> sendList = new ArrayList<>();
        for(GeneticPlayer p:players){
            sendList.add(p.getWeights(false));
        }
        SamuraiList.WeightsList = sendList;
    }



    void write(GeneticPlayer gene){
        write(gene.toString());
    }

    void write(String str){

        File file = new File("huga.txt");
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
            pw.println(str);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

class GeneticPlayer{

   // static int geneSize = 16;
    static int geneSize = 28;
    static int geneAbsMax = 100;
    static int geneNumber = 0;
    int mygeneNumber = 0;

    List<Integer> gene = new ArrayList<>();

    public int count = 0;

    GeneticPlayer(){
        DataStrage strage = new DataStrage();
        for(int i = 0; i < geneSize; i++) {
            gene.add(strage.neoSearchWeights[0][i]);
        }
        mygeneNumber = geneNumber++;
    }

    GeneticPlayer(GeneticPlayer g){
        gene = new ArrayList<>(g.gene);
        mygeneNumber = geneNumber++;

    }


    //近傍個体群の取得
    List<GeneticPlayer> getNearPlayer(){
        List<GeneticPlayer> ret = new ArrayList<>();

        for(int loop = 0; loop < 10; loop++) {


            GeneticPlayer player = new GeneticPlayer(this);
            for (int i = 0; i < geneSize; i++) {
                double rand = Math.random();
                if (rand < 0.6) {
                    continue;
                }
                int d = (int) (Math.random() * 7) - 3;

                player.gene.set(i, player.gene.get(i) + d);
            }
            ret.add(player);
        }
        return ret;
    }

    List<Double> getWeights(boolean isTriple){

        List<Double> ret = new ArrayList<>();



        for(int i = 0; i < geneSize; i++) {
            if(isTriple) {
                for (int j = 0; j < 3; j++) {
                    ret.add(Math.pow(1.5, gene.get(i)));
                }
            }else{
                ret.add(Math.pow(1.5, gene.get(i)));
            }
        }
        return ret;
    }




    @Override
    public String toString() {

        String ret = "count=," + count + " , geneNum = , " +mygeneNumber+"," + gene.toString();
        return ret;

    }
}
