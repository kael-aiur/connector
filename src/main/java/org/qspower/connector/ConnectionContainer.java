package org.qspower.connector;

/**
 * @author kael.
 */
public interface ConnectionContainer {
    
    Connection put(String key, Connection conn);
    Connection get(String key);
    
}
