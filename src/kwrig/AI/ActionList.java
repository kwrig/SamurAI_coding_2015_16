package kwrig.AI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/12.
 */
public class ActionList {

    public static List<List<Integer>> openList = new ArrayList<List<Integer>>();

    public static List<List<Integer>> hiddenList = new ArrayList<List<Integer>>();

    final static int moveStart = 5;
    final static int attackStart=1;

    static private void makeOpenList() {


        /*
        3歩移動
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 8; i++) {

                List<Integer> moveList = new ArrayList<Integer>();
                for (int j = 0; j < 3; j++) {
                    moveList.add(  ((i>>j)&1) + d +moveStart );
                }
                moveList.add(0);
                System.out.println(moveList);
                openList.add(moveList);
            }
        }

        /*
        3歩移動+hide
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 8; i++) {

                List<Integer> moveList = new ArrayList<Integer>();
                for (int j = 0; j < 3; j++) {
                    moveList.add(  ((i>>j)&1) + d +moveStart );
                }
                moveList.add(9);
                moveList.add(0);

                openList.add(moveList);
            }
        }

        /*
        攻撃+移動 移動+攻撃
         */

        for (int d = 0; d <4; d++) {
            for (int md = 0; md < 4; md++) {

                List<Integer> moveList = new ArrayList<Integer>();
                moveList.add(d+attackStart);
                moveList.add(md+moveStart);
                moveList.add(0);
                openList.add(moveList);

                System.out.println(moveList);

                moveList = new ArrayList<Integer>();
                moveList.add(md+moveStart);
                moveList.add(d+attackStart);
                moveList.add(0);
                openList.add(moveList);

                System.out.println(moveList);
            }
        }

         /*
        攻撃+移動+潜伏 移動+攻撃 +潜伏
         */

        for (int d = 0; d <4; d++) {
            for (int md = 0; md < 4; md++) {

                List<Integer> moveList = new ArrayList<Integer>();
                moveList.add(d+attackStart);
                moveList.add(md+moveStart);
                moveList.add(9);
                moveList.add(0);
                openList.add(moveList);

                moveList = new ArrayList<Integer>();
                moveList.add(md+moveStart);
                moveList.add(d+attackStart);
                moveList.add(9);
                moveList.add(0);
                openList.add(moveList);
            }
        }


        System.out.println(openList.size());





    }


    ActionList() {


    }


    public static void main(String[] args) {

        makeOpenList();

    }

}
