package org.simply.connected.application.model;

public class Segment {
    protected final double from;
    protected final double to;

    public Segment(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[" +
                String.format("%.4f", from) +
                " ; " +
                String.format("%.4f", to) +
                "]";
    }
}
