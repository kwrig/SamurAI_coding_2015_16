package kwrig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kwrig on 2016/01/03.
 */
public class General {

    public static int width = 15;
    public static int height = 15;

    public final static int[] ORDER = {0, 3, 4, 1, 2, 5, 3, 0, 1, 4, 5, 2};

    public final static int[] COST = {999, 4, 4, 4, 4, 2, 2, 2, 2, 1, 1, 999};

    public final static int[] MOVE_X = {0, 1, 0, -1};
    public final static int[] MOVE_Y = {1, 0, -1, 0};

    public static String crlf = System.getProperty("line.separator");

    public static List<Set<List<Integer>>> ATTACKRANGE = new ArrayList<>();

    public static List<Set<List<Integer>>> NEARATTACKRANGE = new ArrayList<>();

    public static List<Set<List<Integer>>> NEARNEARATTACKRANGE = new ArrayList<>();

    public final static int[][][][] WEAPON = {
            {
                    {
                            {0, 1}, {0, 2}, {0, 3}, {0, 4}
                    },
                    {
                            {0, 1}, {0, 2}, {1, 0}, {1, 1}, {2, 0}
                    },
                    {
                            {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
                    }
            },
            {
                    {
                            {1, 0}, {2, 0}, {3, 0}, {4, 0}
                    },
                    {
                            {1, 0}, {2, 0}, {0, -1}, {1, -1}, {0, -2}
                    },
                    {
                            {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}
                    }

            },
            {
                    {
                            {0, -1}, {0, -2}, {0, -3}, {0, -4}
                    },
                    {
                            {0, -1}, {0, -2}, {-1, 0}, {-1, -1}, {-2, 0}
                    },
                    {
                            {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}
                    }
            },
            {
                    {
                            {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}
                    },
                    {
                            {-1, 0}, {-2, 0}, {0, 1}, {-1, 1}, {0, 2}
                    },
                    {
                            {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {1, 1}, {0, 1}, {-1, 1}
                    }

            },


    };

    public final static int[][] FIRST_PLACE = {
            {0, 5},
            {0, 14},
            {9, 14},
            {14, 9},
            {14, 0},
            {5, 0},
    };

    public static int[][] getAttackRange(int samuraiNumber, int dist) {

        int n = samuraiNumber % 3;

        /*
        int l = WEAPON[n].length;

        int ret[][] = new int[l ][2];

        for (int i = 0; i < l; i++) {

            switch (dist%4){
                case 0:
                    ret[i][0] = WEAPON[n][i][0];
                    ret[i][1] = WEAPON[n][i][1];
                    break;
                case 1:
                    ret[i][0] = WEAPON[n][i][1];
                    ret[i][1] = -WEAPON[n][i][0];
                    break;
                case 2:
                    ret[i][0] = -WEAPON[n][i][0];
                    ret[i][1] = -WEAPON[n][i][1];
                    break;

                case 3:
                    ret[i][0] = -WEAPON[n][i][1];
                    ret[i][1] = WEAPON[n][i][0];
                    break;
            }

        }
        */

        return WEAPON[dist % 4][n];

    }

    public static boolean isCanAttackRange(int enemyBukiNumner, int enemyPos, int myPos) {

        int[] myPos_ = getXY(myPos);
        int[] enemyPos_ = getXY(enemyPos);

        List<Integer> distList = new ArrayList<>();
        distList.add(enemyPos_[0] - myPos_[0]);
        distList.add(enemyPos_[1] - myPos_[1]);

        return ATTACKRANGE.get(enemyBukiNumner % 3).contains(distList);
    }


    public static int getDist(int pos1, int pos2) {

        return Math.abs(getXY(pos1)[0] - getXY(pos2)[0]) + Math.abs(getXY(pos1)[1] - getXY(pos2)[1]);
    }


    public static int leaveMoveTurnInEqualBuki(int turn, int samuraiNumber) {


        int ret = 0;

        turn++;

        int enemySamurai = (samuraiNumber % 3 + (1 - samuraiNumber / 3) * 3);

        while (ORDER[turn % 12] != enemySamurai) {

            if (ORDER[turn % 12] == samuraiNumber) {
                ret++;
            }
            turn++;

        }
        return ret;

    }

    public static boolean isRangeX(int x) {
        return 0 <= x && x < width;
    }

    public static boolean isRangeY(int y) {
        return 0 <= y && y < height;
    }


    public static int[] getXY(int pos) {
        int[] ret = {pos % width, pos / width};
        return ret;

    }


    private static void makeAttackRange() {

        for (int i = 0; i < 3; i++) {
            ATTACKRANGE.add(new HashSet<>());
        }


        for (int buki = 0; buki < 3; buki++) {
        /*
        4方向について
         */
            for (int dist = 0; dist < 4; dist++) {

                //それぞれの武器について
                int[][] range = getAttackRange(buki, dist);
                for (int[] p : range) {

                    List<Integer> list = new ArrayList<>(3);

                    list.add(p[0]);
                    list.add(p[1]);
                    ATTACKRANGE.get(buki).add(new ArrayList<>(list));
                    list.clear();

                    list.add(p[0] + 1);
                    list.add(p[1]);
                    ATTACKRANGE.get(buki).add(new ArrayList<>(list));
                    list.clear();

                    list.add(p[0]);
                    list.add(p[1] + 1);
                    ATTACKRANGE.get(buki).add(new ArrayList<>(list));
                    list.clear();

                    list.add(p[0] - 1);
                    list.add(p[1]);
                    ATTACKRANGE.get(buki).add(new ArrayList<>(list));
                    list.clear();

                    list.add(p[0]);
                    list.add(p[1] - 1);
                    ATTACKRANGE.get(buki).add(new ArrayList<>(list));
                    list.clear();
                }
            }
        }

        for (int buki = 0; buki < 3; buki++) {

            NEARATTACKRANGE.add(new HashSet<>(256));

            for (List<Integer> attackRange : ATTACKRANGE.get(buki)) {

        /*
        4方向について
         */
                for (int dist = 0; dist < 4; dist++) {
                    List<Integer> list = new ArrayList<>(3);
                    list.add(attackRange.get(0) + General.MOVE_X[dist]);
                    list.add(attackRange.get(1) + General.MOVE_Y[dist]);
                    NEARATTACKRANGE.get(buki).add(list);
                }
            }

            NEARNEARATTACKRANGE.add(new HashSet<>(256));
            for (List<Integer> attackRange : NEARATTACKRANGE.get(buki)) {
                for (int dist = 0; dist < 4; dist++) {
                    List<Integer> list = new ArrayList<>(3);
                    list.add(attackRange.get(0) + General.MOVE_X[dist]);
                    list.add(attackRange.get(1) + General.MOVE_Y[dist]);
                    NEARNEARATTACKRANGE.get(buki).add(list);
                }
            }

        }

    }


    public static int getPosition(int x, int y) {
        x = Math.max(0, Math.min(x, width - 1));
        y = Math.max(0, Math.min(y, height - 1));
        return y * width + x;
    }

    private static boolean init_flag = true;

    public General() {
        if (init_flag) {
            makeAttackRange();

            init_flag = false;
        }
    }

}
