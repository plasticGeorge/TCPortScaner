package com.plasticgeorge.tcpscaner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class MultithreadedScanner implements PortsScanner, Runnable {
    private final ScannerInput input;
    private final Object lock = new Object();
    private final TreeSet<HostPortsStatus> output;

    public MultithreadedScanner(ScannerInput input){
        this.input = input;
        output = new TreeSet<>();
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
        synchronized (lock) {
            if(!input.getHosts().isEmpty())
                host = input.getHosts().pop();
            else
                host = null;
        }
        while(host != null) {
            HashMap<Integer, String> portsStatus = new HashMap<>();
            for (int port : input.getPorts()) {
                boolean isOpen = false;
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 500);
                    isOpen = socket.isConnected();
                } catch (Exception ignored) {

                }
                portsStatus.put(port, isOpen ? "open" : "close");
            }
            synchronized (lock) {
                output.add(new HostPortsStatus(host, portsStatus));
                if(!input.getHosts().isEmpty())
                    host = input.getHosts().pop();
                else
                    host = null;
            }
        }
    }
}
