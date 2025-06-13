package util;

public class Vector {

    private double[] vals;
    private int dim;

    public Vector(int dimension, double... values) {

        if (vals.length != dimension) {
            throw new IllegalArgumentException("Mismatched numbers of coords with dimension");
        }

        dim = dimension;
        vals = new double[dim];

        int i = 0;
        for (double n : values) {
            vals[i] = n;
            i++;
        }
    }

    public static Vector random(int dimension, double magnitude) {
        double[] rands = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            rands[i] = (Math.random()-0.5)*magnitude;
        }
        return new Vector(dimension, rands);
    }

    public double[] getVals() {
        return vals;
    }

    public double get(int value) {
        return vals[value];
    }

    public Vector add(Vector other) {
        double[] added = new double[dim];
        for (int i = 0; i < dim; i++) {
            added[i] = vals[i] + other.getVals()[i];
        }
        return new Vector(dim, added);
    }

    public Vector scale(double scalar) {
        double[] scaled = new double[dim];
        for (int i = 0; i < dim; i++) {
            scaled[i] = vals[i] * scalar;
        }
        return new Vector(dim, scaled);
    }

    public double dot(Vector other) {
        double result = 0;
        for (int i = 0; i < dim; i++) {
            result += vals[i] * other.getVals()[i];
        }
        return result;
    }

    public void set(double... news) {
        int i = 0;
        for (double n : news) {
            vals[i] = n;
            i++;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("(");
        for (int i = 0; i < dim; i++) {
            s.append(vals[i]);
            if (i<dim-1) s.append(", ");
        }
        return s.toString();
    }

    public Vector clone() {
        double[] news = new double[dim];
        int i = 0;
        for (double v : vals) {
            news[i] = v;
            i++;
        }
        return new Vector(dim, news);
    }

    public Vector negative() {
        double[] news = new double[dim];
        int i = 0;
        for (double v : vals) {
            news[i] = -v;
            i++;
        }
        return new Vector(dim, news);
    }

    public void addThis(Vector other) {
        for (int i = 0; i < dim; i++) {
            vals[i] += other.getVals()[i];
        }
    }

    public void scaleThis(double scalar) {
        for (int i = 0; i < dim; i++) {
            vals[i] *= scalar;
        }
    }
}
