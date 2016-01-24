package kwrig;

/**
 * Created by kwrig on 2016/01/03.
 */
public class General {

    public static int width = 15;
    public static int height = 15;

    public final static int[] ORDER = {0,3,4,1,2,5,3,0,1,4,5,2};

    public final static int[] COST = {999,4,4,4,4,2,2,2,2,1,1,999};

    public final static int[] MOVE_X = {0,1,0,-1};
    public final static int[] MOVE_Y = {1,0,-1,0};

    public static String crlf = System.getProperty("line.separator");

    public final static int[][][] WEAPON = {
        {
            {0,1},{0,2},{0,3},{0,4}
        },
        {
            {0,1},{0,2},{1,0},{1,1},{2,0}
        },
        {
            {-1,-1},{-1,0},{-1,1},{0,1},{1,-1},{1,0},{1,1}
        }
    };

    public final static int[][] FIRST_PLACE = {
            {0,5},
            {0,14},
            {9,14},
            {14,9},
            {14,0},
            {5,0},
    };

    public static int[][] getAttackRange(int samuraiNumber,int dist){

        int n = samuraiNumber%3;

        int l = WEAPON[n].length;

        int ret[][] = new int[l ][2];

        for (int i = 0; i < l; i++) {

            switch (dist%4){
                case 0:
                    ret[i][0] = WEAPON[n][i][0];
                    ret[i][1] = WEAPON[n][i][1];
                    break;
                case 1:
                    ret[i][0] = -WEAPON[n][i][1];
                    ret[i][1] = WEAPON[n][i][0];
                    break;
                case 2:
                    ret[i][0] = -WEAPON[n][i][0];
                    ret[i][1] = -WEAPON[n][i][1];
                    break;

                case 3:
                    ret[i][0] = WEAPON[n][i][1];
                    ret[i][1] = -WEAPON[n][i][0];
                    break;
            }

        }

        return ret;

    }

    public static boolean isRangeX(int x){
        return 0<=x&&x<width;
    }
    public static boolean isRangeY(int y){
        return 0<=y&&y<height;
    }


    public static int[] getXY(int pos){
        int[] ret = {pos%width,pos/width};
        return ret;

    }



    public static int getPosition(int x,int y){
        x = Math.max( 0 , Math.min(x,width-1));
        y = Math.max( 0 , Math.min(y,height-1));
        return y*width + x;
    }


}
