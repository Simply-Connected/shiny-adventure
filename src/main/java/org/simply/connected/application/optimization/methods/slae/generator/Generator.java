package org.simply.connected.application.optimization.methods.slae.generator;

import java.io.File;
import java.io.IOException;

public interface Generator {

    void generate(File directory, String generationID) throws IOException;
}
