package org.qspower.connector;

import java.io.IOException;

/**
 * @author kael.
 */
public interface HeartBeater {
    int getIntervalSecond();
    boolean beat() throws IOException;
}
