package org.qspower.connector;

import java.io.IOException;

/**
 * @author kael.
 */
public class SimpleHeartBeater implements HeartBeater {
    private int        intervalSecond;
    private Connection connection;

    public SimpleHeartBeater(Connection connection) {
        this(10, connection);
    }

    public SimpleHeartBeater(int intervalSecond, Connection connection) {
        this.intervalSecond = intervalSecond;
        this.connection = connection;
    }

    public void setIntervalSecond(int intervalSecond) {
        this.intervalSecond = intervalSecond;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int getIntervalSecond() {
        return intervalSecond;
    }

    @Override
    public boolean beat() throws IOException {
        return true;
    }

}
