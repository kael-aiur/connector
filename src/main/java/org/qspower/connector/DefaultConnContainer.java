package org.qspower.connector;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author kael.
 */
public class DefaultConnContainer implements ConnectionContainer {
    private static final Logger log = Logger.getLogger(DefaultConnContainer.class.getName());
    
    private static final ConnectionContainer container       = new DefaultConnContainer();
    private static final AtomicInteger       coreThreadIndex = new AtomicInteger(0);

    private Map<String, ConnectionWrapper> conns = new ConcurrentHashMap<>();

    private Timer           timer;
    private ExecutorService es;

    public static ConnectionContainer getInstance() {
        return container;
    }

    private DefaultConnContainer() {
        init();
    }

    public void init() {
        initTimer();
        initThreadPool();
    }

    protected void initTimer() {
        timer = new Timer();
        timer.schedule(new HeartBeatTask(conns, es, strings -> {
            
        }), 10000, 1000);
    }

    protected void initThreadPool() {
        es = Executors.newFixedThreadPool(5, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("conn-heart-beat-" + coreThreadIndex.incrementAndGet());
            return t;
        });
    }

    @Override
    public Connection put(String key, Connection conn) {
        return conns.put(key, new ConnectionWrapper(conn));
    }

    @Override
    public Connection get(String key) {
        return conns.get(key);
    }

    public static class HeartBeatTask extends TimerTask {
        private Map<String, ConnectionWrapper> conns;
        private ExecutorService                es;
        private Consumer<List<String>>         invalidKeyConsumer;

        public HeartBeatTask(Map<String, ConnectionWrapper> conns, ExecutorService es, Consumer<List<String>> invalidKeyConsumer) {
            this.conns = conns;
            this.es = es;
            this.invalidKeyConsumer = invalidKeyConsumer;
        }

        @Override
        public void run() {
            List<Future<String>> futures = new LinkedList<>();
            conns.forEach((key, value) -> {
                if (value.timingSecondAndCheck()) {
                    Future<String> future = es.submit(new CallableHeartBeater(key,value.getHeartBeater()));
                    futures.add(future);
                }
            });
            List<String> list = futures.stream().map(f -> {
                try {
                    return f.get(3, TimeUnit.SECONDS);
                } catch (TimeoutException | ExecutionException | InterruptedException e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (!list.isEmpty()){
                invalidKeyConsumer.accept(list);
            }
        }
    }
    
    private static class CallableHeartBeater implements Callable<String>{
        
        private String id;
        private HeartBeater beater;

        public CallableHeartBeater(String id, HeartBeater beater) {
            this.id = id;
            this.beater = beater;
        }

        @Override
        public String call() throws Exception {
            if(beater.beat()){
                return null;
            }else {
                return this.id;
            }
        }
    }
    
}
