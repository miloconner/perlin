package util;

import java.util.Arrays;
import java.util.Random;

public class Perlin {

    private double block;
    private long seed;
    private int dim;

    public Perlin(long seed, double block, int dimension) {
        this.seed = seed;
        this.block = block;
        this.dim = dimension;
    }

    private double[] getAnglesAt(long seed, int... coords) {
        double[] rands = new double[dim-1];
        for (int i = dim; i > 1; i--) {
            long randVal = seed;
            for (int j = 0; 0 < i; j++) {
                randVal = (long)Math.pow(randVal, coords[j]);
            }
            Random rand = new Random(randVal);
            rands[i] = rand.nextDouble() * 2 * Math.PI;
        }
        return rands;
    }

    private double smooth(double s) {
        return 6*Math.pow(s, 5) - 15*Math.pow(s, 4) + 10*Math.pow(s, 3);
    }

    private Vector[] genCorners(Vector minimum) {
        Vector[] corners = new Vector[dim];

        for (int i = 0; i < Math.pow(2, dim); i++) {
            double[] coords = new double[dim];
            for (int d = 0; d < dim; d++) {
                boolean isMax = ((i>>d) & 1) == 1;
                coords[d] = isMax ? minimum.get(d) + 1 : minimum.get(d);
            }
            corners[i] = new Vector(dim, coords);
        }
        return corners;
    }

    public double retrieve(double... coords) {
        Vector pos = new Vector(dim, coords);
        Vector[] corners = new Vector[dim];
        double[] min = new double[dim];
        for (int i = 0; i < dim; i++) {
            min[i] = (int)Math.floor(coords[i]/block);
        }
        Vector minVec = new Vector(dim, min);
        corners = genCorners(minVec);

        Vector[] gradVecs = new Vector[(int)Math.pow(2, dim)];
        Vector[] offVecs = new Vector[(int)Math.pow(2, dim)];
        for (int i = 0; i < Math.pow(2, dim); i++) {
            double[] thetas = getAnglesAt(seed, Arrays.stream(corners[i].getVals()).mapToInt(d -> (int) d).toArray()); //gets the corners values, just casts to int using stream
            
            double[] comps = new double[dim];
            double prod = 1;

            for (int j = 0; j < dim-1; j++) {
                comps[j] = prod * Math.cos(thetas[j]);
                prod *= Math.sin(thetas[j]);
            }

            comps[dim-1] = prod;
            gradVecs[i] = new Vector(dim, comps);
            offVecs[i] = pos.add(corners[i].negative());
        }

        Vector s = pos.scale(1/block).add(minVec.negative());
        Vector f = new Vector(dim, Arrays.stream(s.getVals()).map(x -> smooth(x)).toArray());

        double[] d = new double[(int)Math.pow(2,dim)];
        for (int i = 0; i < Math.pow(2, dim); i++) {
            d[i] = gradVecs[i].dot(offVecs[i]);
        }

        //2d implementation
        double i1 = d1 + f1*(d2 - d1);
        double i2 = d3 + f1*(d4 - d3);
        double finalValue = i1 + f2*(i2 - i1);

        return finalValue;
    }
}
