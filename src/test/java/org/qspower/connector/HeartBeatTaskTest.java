package org.qspower.connector;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author kael.
 */
public class HeartBeatTaskTest extends TestBase {

    private static final Logger log = Logger.getLogger(HeartBeatTaskTest.class.getName());

    @Test
    public void testHeartBeat() throws IOException {
        ExecutorService server = Executors.newFixedThreadPool(2);
        ServerSocket ss1 = new ServerSocket(6661);
        ServerSocket ss2 = new ServerSocket(6662);
        server.execute(() -> {

        });

        Map<String, ConnectionWrapper> conns = new ConcurrentHashMap<>();
        ConnectionFactory factory = FactoryBuilder.create().build();
        conns.put("1", new ConnectionWrapper(factory.createConnection(ss1.getInetAddress().getHostAddress(), ss1.getLocalPort())));
        conns.put("2", new ConnectionWrapper(factory.createConnection(ss2.getInetAddress().getHostAddress(), ss2.getLocalPort())));
        ExecutorService es = Executors.newFixedThreadPool(1);

        DefaultConnContainer.HeartBeatTask task = new DefaultConnContainer.HeartBeatTask(conns, es, strings -> {

        });
        task.run();
    }

    private class MockServer implements Runnable {
        private ServerSocket ss;

        public MockServer(ServerSocket ss) {
            this.ss = ss;
        }

        @Override
        public void run() {
            while (true) {
                Socket socket;
                try {
                    socket = ss.accept();
                    InputStream is = socket.getInputStream();
                    
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                    continue;
                }
            }
        }
    }

}
