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
                String.format("%.4f", from) +
                " ; " +
                String.format("%.4f", mid) +
                " ; " +
                String.format("%.4f", to) +
                "]";
    }
}
