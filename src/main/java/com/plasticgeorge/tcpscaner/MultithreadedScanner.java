package com.plasticgeorge.tcpscaner;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

public class MultithreadedScanner implements PortsScanner, Runnable {
    final LinkedList<String> hosts;
    final LinkedList<Integer> ports;
    final int threadsCount;
    final Object lock = new Object();
    final JSONObject output = new JSONObject();

    public MultithreadedScanner(LinkedList<String> hosts, LinkedList<Integer> ports, int threadsCount){
        this.hosts = hosts;
        this.ports = ports;
        this.threadsCount = threadsCount;
    }

    public MultithreadedScanner(Object[] processorOutput){
        this.hosts = (LinkedList<String>) processorOutput[0];
        this.ports = (LinkedList<Integer>) processorOutput[1];
        this.threadsCount = (int) processorOutput[2];
    }

    public void scan(){
        Thread[] threads = new Thread[threadsCount];
        for(int i = 0; i < threadsCount; i++) {
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
        try (FileWriter file = new FileWriter(System.getProperty("user.dir") + "\\ports.json")){
            file.write(output.toJSONString());
            file.flush();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void run(){
        String host;
        synchronized (lock) {
            if(!hosts.isEmpty())
                host = hosts.pop();
            else
                host = null;
        }
        while(host != null) {
            JSONObject portsObj = new JSONObject();
            for (int port : ports) {
                boolean isOpen = false;
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 300);
                    isOpen = socket.isConnected();
                } catch (Exception ignored) {

                }
                portsObj.put(port, isOpen ? "open" : "close");
            }
            synchronized (lock) {
                output.put(host, portsObj);
                if(!hosts.isEmpty())
                    host = hosts.pop();
                else
                    host = null;
            }
        }
    }
}
