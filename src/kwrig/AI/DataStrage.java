package kwrig.AI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwrig on 2016/02/08.
 */
public class DataStrage {


    List<Double> weights;

    public DataStrage() {
        weights = simpleWeights();
    }

    public int[][] searchWeights = {


            {-12, 9, -8, 8, -1, -5, -8, -3, 4, 9, -9, -10, 8, -4, -4, -1},
            {-12, 10, -8, 9, -1, -2, -8, -3, 4, 7, -10, -6, 8, -8, -4, 0},
            {-12, 9, -8, 8, -1, -3, -8, -3, 4, 9, -9, -10, 8, -4, -4, -1},
            {-31, 31, -8, 8, -4, -16, -10, -5, 16, 22, -7, -8, 15, 7, -28, 12}


    };

    public int[][] neoSearchWeights = {
            {-2, -8, -5, 2, 14, 16, 12, 10, 21, 13, 5, 13, -7, -4, 5, -17, 0, -5, -1, -21, 2, -13, -9, -5, -3, -7, -17, -36},
            {14, -5, 4, 4, 15, 8, 4, 0, 11, 7, 9, 0, -8, -12, -11, -8, -2, -10, 0, -3, 3, -10, -11, -3, 8, -13, -11, -25},
            {17, -6, 4, 3, 11, 6, 4, -2, 10, 9, 9, 0, -8, -11, -8, -9, -2, -10, 3, -4, 1, -9, -14, -3, 10, -13, -12, -26},
            {-2, -8, -5, 2, 14, 16, 12, 10, 21, 13, 5, 13, -7, -4, 5, -17, 0, -5, -1, -21, 2, -13, -9, -5, -3, -7, -17, -36},
            {-6, -8, -2, -1, 12, 16, 15, 8, 21, 13, 6, 12, -4, -4, 5, -17, 0, -8, -3, -21, 2, -15, -9, -5, -3, -7, -14, -36},
            {26, -4, -2, 1, 15, 6, 7, -6, 10, 4, 6, 0, -8, -12, -3, -7, -6, -8, 3, 0, 2, -9, -16, -6, 8, -10, -18, -23},
            {25, -3, -5, 1, 15, 3, 7, -9, 9, 9, 6, 0, -7, -13, -3, -9, -6, -8, 3, 0, 4, -6, -16, -6, 11, -10, -17, -23},
            {96, 24, -11, 29, 89, 11, 95, 25, 43, 41, 48, 8, -14, -60, -4, -39, -3, 9, -41, -19, 6, -95, -38, -19, 24, -46, -39, -46},


    };

    List<Double> simpleWeights() {

        List<Double> ret = new ArrayList<>();

        ret = addTriple(ret, 1.0 / 5.0);
        ret = addTriple(ret, 300);
        ret = addTriple(ret, 0.01);
        ret = addTriple(ret, 200);
        ret = addTriple(ret, 1);
        ret = addTriple(ret, 1 / 10.0);//5
        ret = addTriple(ret, 1 / 4.001);
        ret = addTriple(ret, 1.0);
        ret = addTriple(ret, 10.0);
        ret = addTriple(ret, 11.0);
        ret = addTriple(ret, 15.0);//10
        ret = addTriple(ret, 10.0);
        ret = addTriple(ret, 15);
        ret = addTriple(ret, 10);
        ret = addTriple(ret, 100);
        ret = addTriple(ret, 50);
        return ret;
    }

    List<Double> addTriple(List<Double> list, double w) {

        for (int i = 0; i < 3; i++) {
            list.add(w);
        }
        return list;

    }

    public List<Double> getWeights() {
        return weights;
    }

    public List<Double> getWeights(int n) {

        List<Double> ret = new ArrayList<>();

        for (int i = 0; i < searchWeights[n].length; i++) {
            for (int j = 0; j < 3; j++) {
                ret.add(Math.pow(1.5, searchWeights[n][i]));
            }
        }
        return ret;
    }

    public List<Double> getWeights(int n, boolean isNeo) {
        if (isNeo == false) {
            return getWeights(n);
        }


        List<Double> ret = new ArrayList<>();

        for (int i = 0; i < neoSearchWeights[n].length; i++) {
            ret.add(Math.pow(1.5, neoSearchWeights[n][i]));
        }
        return ret;

    }

}
