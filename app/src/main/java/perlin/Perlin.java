package perlin;

import java.util.Random;

import util.Vec2;

public class Perlin {

    private double block;
    private long seed;

    public Perlin(long seed, double block) {
        this.seed = seed;
        this.block = block;
    }

    public double getValueAt(int x, int y, long seed) {
        Random rand = new Random(seed ^ (x * 73856093L) ^ (y * 19349663L));
        return rand.nextDouble() * 2 * Math.PI;
    }

    public double smooth(double s) {
        return 6*Math.pow(s, 5) - 15*Math.pow(s, 4) + 10*Math.pow(s, 3);
        // return 3 * Math.pow(x, 2) - 2 * Math.pow(x, 3);
        // System.out.println("x is " + x);
        // System.out.println(8 * Math.pow(x, 7) - 7*Math.pow(x, 8));
        // if (x >= 1) {
        //     System.out.println(x);
        // } else if (x < 0) {
        //     System.out.println("Negative x: " + x);
        // }
        // return 8 * Math.pow(x, 7) - 7*Math.pow(x, 8);
    }

    public double retrieve(int x, int y) {
        int c1 = (int)Math.floor(x/block);
        int c2 = c1 + 1;
        int c3 = (int)Math.floor(y/block);
        int c4 = c3 + 1;
        double theta1 = getValueAt(c1,c3, seed);
        Vec2 gradVec1 = new Vec2(Math.cos(theta1), Math.sin(theta1));
        Vec2 offVec1 = new Vec2(x/block - c1, y/block - c3);
        double theta2 = getValueAt(c2,c3, seed);
        Vec2 gradVec2 = new Vec2(Math.cos(theta2), Math.sin(theta2));
        Vec2 offVec2 = new Vec2(x/block - c2, y/block - c3);
        double theta3 = getValueAt(c1,c4, seed);
        Vec2 gradVec3 = new Vec2(Math.cos(theta3), Math.sin(theta3));
        Vec2 offVec3 = new Vec2(x/block - c1, y/block - c4);
        double theta4 = getValueAt(c2,c4, seed);
        Vec2 gradVec4 = new Vec2(Math.cos(theta4), Math.sin(theta4));
        Vec2 offVec4 = new Vec2(x/block - c2, y/block - c4);
        //System.out.println(d1);

        double sx = x/block - c1;
        double sy = y/block - c3;

        double f1 = smooth(sx);
        double f2 = smooth(sy);

        // double f1 = 6*Math.pow(sx, 5) - 15*Math.pow(sx, 4) + 10*Math.pow(sx, 3);
        // double f2 = 6*Math.pow(sy, 5) - 15*Math.pow(sy, 4) + 10*Math.pow(sy, 3);

        double d1 = gradVec1.dot(offVec1);
        double d2 = gradVec2.dot(offVec2);
        double d3 = gradVec3.dot(offVec3);
        double d4 = gradVec4.dot(offVec4);
        double i1 = d1 + f1*(d2 - d1);
        double i2 = d3 + f1*(d4 - d3);
        double finalValue = i1 + f2*(i2 - i1);

        return finalValue;
    }
}
