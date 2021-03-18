package org.simply.connected.application.model;

public class TriplePoint extends Segment {
    private final double mid;

    public double getMid() {
        return mid;
    }

    public TriplePoint(double from, double middle, double to) {
        super(from, to);
        this.mid = middle;
    }

    @Override
    public String toString() {
        return "[" +
                String.format("%.5f", from) +
                " ; " +
                String.format("%.5f", mid) +
                " ; " +
                String.format("%.5f", to) +
                "]";
    }
}
