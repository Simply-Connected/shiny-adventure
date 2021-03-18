package org.simply.connected.application.optimization.methods.stepped;


import org.simply.connected.application.model.Segment;

public interface SteppedOptimizationMethod {
    Segment getCurrSegment();

    boolean minimize();
}
