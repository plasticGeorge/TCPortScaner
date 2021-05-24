package com.plasticgeorge.tcpscaner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolScanner implements PortsScanner {
    private ScannerInput input;
    private ExecutorService threadPool;
    private final TreeSet<HostPortsStatus> output;
    private final ReentrantLock locker = new ReentrantLock();

    public ThreadPoolScanner(ScannerInput input){
        this.input = input;
        threadPool = Executors.newFixedThreadPool(input.threadsNum);
        output = new TreeSet<>();
    }

    @Override
    public TreeSet<HostPortsStatus> scan() {
        LinkedList<String> hosts = input.hosts;
        LinkedList<Integer> ports = input.ports;
        int threadsNum = input.threadsNum;
        Future[] futures = new Future[threadsNum];
        for(int i = 0; i < threadsNum; i++) {
            futures[i] = threadPool.submit(() -> {
                locker.lock();
                boolean proceed = !hosts.isEmpty();
                locker.unlock();
                while (proceed) {
                    String host = hosts.pop();
                    HashMap<Integer, String> portsStatus = new HashMap<>();
                    for (int port : ports) {
                        try (Socket socket = new Socket()) {
                            socket.connect(new InetSocketAddress(host, port), 500);
                            portsStatus.put(port, "open");
                        } catch (Exception ignored) {
                            portsStatus.put(port, "close");
                        }
                    }
                    output.add(new HostPortsStatus(host, portsStatus));
                    locker.lock();
                    proceed = !hosts.isEmpty();
                    locker.unlock();
                }
            });
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()){

        }
        return output;
    }
}
