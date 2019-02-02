package org.qspower.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author kael.
 */
public class SocketConnection implements Connection {

    private Sync        sync = new Sync();
    private HeartBeater heartBeater;
    private Socket      socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
        this.heartBeater = HeartBeaterBuilder.create().withConnection(this).build();
    }

    @Override
    public HeartBeater getHeartBeater() {
        return heartBeater;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public boolean release() {
        return sync.tryRelease(1);
    }

    @Override
    public boolean acquire() {
        return sync.tryAcquire(1);
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    private class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1915301135687142034L;

        @Override
        protected boolean tryAcquire(int arg) {
            if (1 == getState()) {
                return compareAndSetState(1, 0);
            } else {
                return false;
            }
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (0 == getState()) {
                return compareAndSetState(0, 1);
            } else {
                return false;
            }
        }
    }

}
