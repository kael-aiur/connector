package org.qspower.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author kael.
 */
public interface Connection extends AutoCloseable {
    boolean release();
    boolean acquire();
    
    HeartBeater getHeartBeater();
    
    InputStream openInputStream() throws IOException;
    OutputStream openOutputStream() throws IOException;
}
