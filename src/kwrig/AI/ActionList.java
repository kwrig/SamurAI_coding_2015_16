package kwrig.AI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/01/12.
 */
public class ActionList {

    public static List<List<Integer>> openMaxList = new ArrayList<List<Integer>>();

    public static List<List<Integer>> openMiniMoveList = new ArrayList<>();

    public static List<List<Integer>> hiddenList = new ArrayList<List<Integer>>();

    public static List<List<Integer>> openAllList = new ArrayList<>();

    public static List<List<Integer>> hiddenAllList = new ArrayList<>();

    public static List<List<Integer>> hiddenMiniMoveList = new ArrayList<>();

    public static List<List<Integer>> openAttackAllList = new ArrayList<>();
    public static List<List<Integer>> hiddenAttackAllList = new ArrayList<>();


    final static int moveStart = 5;
    final static int attackStart = 1;

    static boolean initFlag = true;

    /*
    全部無駄なく動くやつだけ
     */
    static private void makeMaxOpenList() {
        /*
        3歩移動 一種のみ
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 4; i++) {

                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                        List<Integer> moveList = new ArrayList<Integer>();
                        for (int j = 0; j < 3; j++) {
                            moveList.add((((i >> j) & 1) + d) % 4 + moveStart);
                        }
                        moveList.add(0);
                        openMaxList.add(moveList);
                        break;
                }
            }
        }

        /*
        3歩移動+hide
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 4; i++) {

                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                        List<Integer> moveList = new ArrayList<Integer>();
                        for (int j = 0; j < 3; j++) {
                            moveList.add((((i >> j) & 1) + d) % 4 + moveStart);
                        }
                        moveList.add(9);
                        moveList.add(0);

                        openMaxList.add(moveList);
                }
            }
        }
        /*
        攻撃+移動 移動+攻撃
         */

        for (int d = 0; d < 4; d++) {
            for (int md = 0; md < 4; md++) {

                List<Integer> moveList = new ArrayList<Integer>();
                moveList.add(d + attackStart);
                moveList.add(md + moveStart);
                moveList.add(0);
                openMaxList.add(moveList);
                openAttackAllList.add(moveList);


                moveList = new ArrayList<>();
                moveList.add(md + moveStart);
                moveList.add(d + attackStart);
                moveList.add(0);
                openMaxList.add(moveList);
                openAttackAllList.add(moveList);
            }
        }

         /*
        攻撃+移動+潜伏 移動+攻撃 +潜伏
         */

        for (int d = 0; d < 4; d++) {
            for (int md = 0; md < 4; md++) {

                List<Integer> moveList = new ArrayList<Integer>();
                moveList.add(d + attackStart);
                moveList.add(md + moveStart);
                moveList.add(9);
                moveList.add(0);
                openMaxList.add(moveList);

                moveList = new ArrayList<>();
                moveList.add(md + moveStart);
                moveList.add(d + attackStart);
                moveList.add(9);
                moveList.add(0);
                openMaxList.add(moveList);
            }
        }


    }

    //ちょっと無駄な動きの分
    static private void makeMiniOpenList() {


        /*
        2歩移動 一種のみ hideは必ず
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 4; i++) {

                switch (i) {
                    case 0:
                    case 1:
                        List<Integer> moveList = new ArrayList<Integer>();
                        for (int j = 0; j < 2; j++) {
                            moveList.add((((i >> j) & 1) + d) % 4 + moveStart);
                        }
                        moveList.add(9);
                        moveList.add(0);
                        openMiniMoveList.add(moveList);
                        break;
                }
            }
        }

        /*
        一歩移動 hideする
         */
        for(int i = 0; i < 4; i++) {
            List<Integer> moveList = new ArrayList<Integer>();
            moveList.add(i+moveStart);
            moveList.add(9);
            moveList.add(0);
            openMiniMoveList.add(moveList);
        }
        /*
        attackのみ hideは必ず
         */
        for(int i = 0; i < 4; i++) {
            List<Integer> moveList = new ArrayList<Integer>();
            moveList.add(i+attackStart);
            moveList.add(9);
            moveList.add(0);
            openMiniMoveList.add(moveList);
        }

        /*
        hideするだけ
         */
        List<Integer> moveList = new ArrayList<Integer>();
        moveList.add(9);
        moveList.add(0);
        openMiniMoveList.add(moveList);



    }


    /*
    現れて移動 //一種
    現れず移動 全種
    現れて移動と攻撃

    現れて攻撃して隠れる

    現れて 二歩移動して 隠れる

     */
    static private void makeHiddenList() {

        /*
        3歩移動 一種のみ
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 8; i++) {

                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                        List<Integer> moveList = new ArrayList<Integer>();
                        moveList.add(10);
                        for (int j = 0; j < 3; j++) {
                            moveList.add((((i >> j) & 1) + d) % 4 + moveStart);
                        }
                        moveList.add(0);
                        hiddenList.add(moveList);
                        break;
                }
            }
        }

          /*
        3歩移動 全種
         */
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 8; i++) {


                List<Integer> moveList = new ArrayList<Integer>();
                for (int j = 0; j < 3; j++) {
                    moveList.add((((i >> j) & 1) + d) % 4 + moveStart);
                }
                moveList.add(0);
                hiddenList.add(moveList);

            }
        }

        /*
        現れて移動と攻撃 現れて攻撃と移動
         */

        for (int d = 0; d < 4; d++) {
            for (int md = 0; md < 4; md++) {

                List<Integer> moveList = new ArrayList<Integer>();
                moveList.add(10);
                moveList.add(d + attackStart);
                moveList.add(md + moveStart);
                moveList.add(0);
                hiddenList.add(moveList);
                hiddenAttackAllList.add(moveList);


                moveList = new ArrayList<>();
                moveList.add(10);
                moveList.add(md + moveStart);
                moveList.add(d + attackStart);
                moveList.add(0);
                hiddenList.add(moveList);
                hiddenAttackAllList.add(moveList);
            }
        }

        /*
        現れて攻撃して隠れる
         */
        for (int d = 0; d < 4; d++) {

            List<Integer> moveList = new ArrayList<Integer>();
            moveList.add(10);
            moveList.add(d + attackStart);
            moveList.add(9);
            moveList.add(0);
            hiddenList.add(moveList);
        }
        /*
        現れて二歩移動して隠れる
         */

        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < 4; i++) {

                switch (i) {
                    case 0:
                    case 1:
                        List<Integer> moveList = new ArrayList<Integer>();
                        moveList.add(10);
                        for (int j = 0; j < 2; j++) {
                            moveList.add((((i >> j) & 1) + d) % 4 + moveStart);
                        }
                        moveList.add(9);
                        moveList.add(0);
                        hiddenList.add(moveList);

                        break;
                }
            }
        }


    }

    public ActionList() {

        if (initFlag) {
            makeMaxOpenList();
            makeMiniOpenList();
            makeHiddenList();
            openAllList.addAll(openMaxList);
            openAllList.addAll(openMiniMoveList);

            hiddenAllList.addAll(hiddenList);

         /*
        現れて一歩移動 hideする
         */
            for(int i = 0; i < 4; i++) {
                List<Integer> moveList = new ArrayList<Integer>();
                moveList.add(10);
                moveList.add(i+moveStart);
                moveList.add(9);
                moveList.add(0);
                hiddenAllList.add(moveList);
            }
            initFlag = false;
        }
    }

    public static void main(String[] args) {

        ActionList actionList = new ActionList();



        System.out.println(openAllList.size());
        System.out.println(hiddenAllList.size());
        System.out.println(openAttackAllList.size());
        System.out.println(hiddenAttackAllList.size());

    }

    private static void print(List<List<Integer>> list) {
        for (List<Integer> ll : list) {
            System.out.println(ll.toString());
        }


    }

}
