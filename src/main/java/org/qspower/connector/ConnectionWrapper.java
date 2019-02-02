package org.qspower.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kael.
 */
class ConnectionWrapper implements Connection {

    private final Connection    connection;
    private       AtomicInteger timer;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
        initTimer();
    }

    private void initTimer() {
        if (connection.getHeartBeater() == null) {
            this.timer = null;
        } else {
            int interval = (connection.getHeartBeater().getIntervalSecond());
            if (interval <= 0) {
                this.timer = null;
            } else {
                this.timer = new AtomicInteger(interval);
            }
        }
    }

    @Override
    public boolean release() {
        return connection.release();
    }

    @Override
    public boolean acquire() {
        return connection.acquire();
    }

    @Override
    public HeartBeater getHeartBeater() {
        return connection.getHeartBeater();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return connection.openInputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return connection.openOutputStream();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public String getId() {
        return connection.getId();
    }

    protected boolean timingSecondAndCheck() {
        if (null != timer && 0 >= timer.decrementAndGet()) {
            initTimer();
            return true;
        } else {
            return false;
        }
    }

}
