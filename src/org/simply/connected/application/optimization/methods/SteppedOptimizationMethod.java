package org.simply.connected.application.optimization.methods;


import org.simply.connected.application.model.Segment;

public interface SteppedOptimizationMethod {
    Segment getCurrSegment();

    boolean minimize();
}
