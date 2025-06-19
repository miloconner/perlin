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
        for (int i = 0; i < dim - 1; i++) {
            long randVal = seed;
            for (int j = i - 1; j > 0; j--) {
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
        Vector[] corners = new Vector[(int)Math.pow(2, dim)];

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
            double length = gradVecs[i].magnitude();
            if (Math.abs(length - 1.0) > 1e-6) {
                System.err.println("Non-unit gradient: " + length);
            }
            // System.out.println(length);
            offVecs[i] = pos.add(corners[i].negative().scale(block)).scale(1/block); //corners are in blocks so must be scaled up and then the dot must be scaled down so back in unit size
        
        }

        Vector s = pos.scale(1/block).add(minVec.negative());
        Vector f = new Vector(dim, Arrays.stream(s.getVals()).map(x -> smooth(x)).toArray());

        double[] d = new double[(int)Math.pow(2,dim)];
        for (int i = 0; i < Math.pow(2, dim); i++) {
            d[i] = gradVecs[i].dot(offVecs[i]);
        }


        double[] temps = d;
        for (int i = dim; i > 0; i--) { //perform lerps
            double[] interps = new double[(int)Math.pow(2, i)/2];
            for (int j = 0; j < Math.pow(2, i-1); j++) {
                interps[j] = temps[j] + f.getVals()[i-1]*(temps[(int)Math.pow(2,i)-j-1] - temps[j]);
            }
            temps = interps;
        }

        System.out.println(temps[0]);
        return temps[0];
    }
}
