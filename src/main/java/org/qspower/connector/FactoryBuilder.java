package org.qspower.connector;

/**
 * @author kael.
 */
public class FactoryBuilder {
    
    public static FactoryBuilder create(){
        return new FactoryBuilder();
    }
    
    public ConnectionFactory build(){
        return new DefaultConnFactory();
    }
    
}
