package com.plasticgeorge.tcpscaner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.TreeSet;

public class MultithreadedScanner implements PortsScanner, Runnable {
    private ScannerInput input;
    private final Object lock = new Object();
    private final TreeSet<HostPortsStatus> output;

    public MultithreadedScanner(ScannerInput input){
        this.input = input;
        output = new TreeSet<>();
    }

    public TreeSet<HostPortsStatus> scan(){
        Thread[] threads = new Thread[input.threadsNum];
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
            if(!input.hosts.isEmpty())
                host = input.hosts.pop();
            else
                host = null;
        }
        while(host != null) {
            HashMap<Integer, String> portsStatus = new HashMap<>();
            for (int port : input.ports) {
                boolean isOpen = false;
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 500);
                    isOpen = socket.isConnected();
                } catch (Exception ignored) {

                }
                portsStatus.put(port, isOpen ? "open" : "close");
            }
            output.add(new HostPortsStatus(host, portsStatus));
            synchronized (lock) {
                if(!input.hosts.isEmpty())
                    host = input.hosts.pop();
                else
                    host = null;
            }
        }
    }
}
