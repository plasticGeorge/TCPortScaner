package com.plasticgeorge.tcpscaner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class MultithreadedScanner implements PortsScanner, Runnable {
    private final ScannerInput input;
    private final Set<HostPortsStatus> output;

    public MultithreadedScanner(ScannerInput input){
        this.input = input;
        output = new ConcurrentSkipListSet<>();
    }

    public Set<HostPortsStatus> scan(){
        Thread[] threads = new Thread[input.getThreadsNum()];
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(this);
            threads[i].start();
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    public void run(){
        String host;
        ConcurrentLinkedQueue<String> hosts = new ConcurrentLinkedQueue<>(input.getHosts());
        LinkedList<Integer> ports = input.getPorts();
        while((host = hosts.poll()) == null) {
            HashMap<Integer, String> portsStatus = new HashMap<>();
            for (int port : input.getPorts()) {
                boolean isOpen = false;
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 500);
                    isOpen = socket.isConnected();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                portsStatus.put(port, isOpen ? "open" : "close");
            }
            output.add(new HostPortsStatus(host, portsStatus));
        }
    }
}
