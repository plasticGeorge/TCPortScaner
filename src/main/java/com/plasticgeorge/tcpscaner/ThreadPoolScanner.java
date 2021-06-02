package com.plasticgeorge.tcpscaner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolScanner implements PortsScanner {
    private final Logger logger = LogManager.getLogger(ThreadPoolScanner.class);

    private final ScannerInput input;
    private final ExecutorService threadPool;
    private final ConcurrentSkipListSet<HostPortsStatus> output;

    public ThreadPoolScanner(ScannerInput input){
        logger.info("ThreadPoolScanner instance created");
        this.input = input;
        threadPool = Executors.newFixedThreadPool(input.getThreadsNum());
        output = new ConcurrentSkipListSet<>();
    }

    @Override
    public Set<HostPortsStatus> scan() {
        logger.info("Ports scan started");
        ConcurrentLinkedQueue<String> hosts = new ConcurrentLinkedQueue<>(input.getHosts());
        LinkedList<Integer> ports = input.getPorts();
        int threadsNum = input.getThreadsNum();
        Future[] futures = new Future[threadsNum];
        for(Future f : futures)
            f = threadPool.submit(() -> {
                try {
                    logger.info("New thread instance");
                    String host;
                    HashMap<Integer, String> portsStatus;
                    while (true) {
                        if((host = hosts.poll()) == null) {
                            logger.info("Work complete");
                            return;
                        }
                        portsStatus = new HashMap<>();
                        for (int port : ports) {
                            logger.info("Now scanning " + host + ":" + port);
                            try (Socket socket = new Socket()) {
                                socket.connect(new InetSocketAddress(host, port), 500);
                                portsStatus.put(port, "open");
                            } catch (Exception ignored) {
                                portsStatus.put(port, "close");
                            }
                        }
                        output.add(new HostPortsStatus(host, portsStatus));
                    }
                } catch (Exception ignore) {
                    logger.warn("Exception caught", ignore);
                }
            });
        threadPool.shutdown();
        while (!threadPool.isTerminated()){
        }
        logger.info("Scan completed");
        return output;
    }
}
