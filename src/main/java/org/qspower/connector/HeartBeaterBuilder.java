package org.qspower.connector;

/**
 * @author kael.
 */
public class HeartBeaterBuilder {
    
    private Connection connection;
    
    public static HeartBeaterBuilder create(){
        return new HeartBeaterBuilder();
    }
    
    public HeartBeaterBuilder withConnection(Connection connection){
        this.connection = connection;
        return this;
    }
    
    
    public HeartBeater build(){
        return new SimpleHeartBeater(connection);
    }
}
