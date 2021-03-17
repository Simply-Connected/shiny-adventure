package org.simply.connected.application.optimization.methods.stepped;

import org.simply.connected.application.model.Segment;
import org.simply.connected.application.model.TriplePoint;

import java.util.function.Function;

public class ParabolicMethod extends AbstractSteppedOptimizationMethod {


    private double x1;
    private double x2;
    private double x3;
    private double minX;

    private double y1;
    private double y2;
    private double y3;
    private double minY;


    public ParabolicMethod(Function<Double, Double> function, Double eps, Segment segment) {
        super(function, eps, new TriplePoint(
                segment.getFrom(),
                (segment.getFrom() + segment.getTo()) / 2,
                segment.getTo())
        );

        x1 = segment.getFrom();
        x2 = (segment.getFrom() + segment.getTo()) / 2;
        x3 = segment.getTo();

        y1 = function.apply(x1);
        y2 = function.apply(x2);
        y3 = function.apply(x3);

        minX = getMinX();
        minY = getMinY();
    }

    private double getMinX() {
        return x1 + x2 - getA1() / getA2();
    }

    private Double getMinY() {
        return function.apply(minX);
    }


    private double getA1() {
        return (y2 - y1) / (x2 - x1);
    }

    private double getA2() {
        return ((y3 - y1) / (x3 - x1) - getA1()) / (x3 - x2);
    }


    public Function<Double, Double> getParabola() {
        return (x) -> y1 + getA1() * (x - x1) + getA2() * (x - x1) * (x - x2);
    }


    @Override
    public boolean minimize() {

        if (less(x1, minX, x2, x3) && minY >= y2) {
            x1 = minX;
            y1 = minY;
        }
        if (less(x1, minX, x2, x3) && minY < y2) {
            x3 = x2;
            y3 = y2;
            x2 = minX;
            y2 = minY;
        }
        if (less(x1, x2, minX, x3) && y2 >= minY) {
            x1 = x2;
            y1 = y2;
            x2 = minX;
            y2 = minY;
        }
        if (less(x1, x2, minX, x3) && y2 < minY) {
            x3 = minX;
            y3 = minY;
        }


        if (Math.abs(getMinX() - minX) <= eps) {
            minX = getMinX();
            minY = getMinY();
            currSegment = new TriplePoint(x1, x2, x3);
            return false;
        }

        minX = getMinX();
        minY = getMinY();

        currSegment = new TriplePoint(x1, x2, x3);
        return true;
    }

    private boolean less(double x1, double x2, double x3, double x4) {
        return x1 < x2 && x2 < x3 && x3 < x4;
    }

}
