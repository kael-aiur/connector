package org.qspower.connector;

/**
 * @author kael.
 */
public interface ConnectionFactory {
    
    Connection createConnection(String host, int port);
    
}
