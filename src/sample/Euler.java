package sample;

public class Euler {

    double h;
    double g;
    double l;
    double k;
    double m;

    public Euler(double h, double g, double l, double k, double m) {
        this.h = h;
        this.g = g;
        this.l = l;
        this.k = k;
        this.m = m;
    }

    public DataPoint solve(DataPoint oldData) {
        oldData.theeta *= Math.PI / 180;
        double theeta = oldData.theeta + h * (oldData.v);
        double v = oldData.v - (h * ((g / l) * Math.sin(oldData.theeta) + (k / m) * oldData.v));

        theeta *= 180 / Math.PI;
        oldData.theeta *= 180 / Math.PI;
        return new DataPoint(theeta, v);
    }
}
