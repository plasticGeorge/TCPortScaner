package com.plasticgeorge.tcpscaner;

import java.util.LinkedList;
import java.util.Objects;

public class ScannerInput {
    LinkedList<String> hosts;
    LinkedList<Integer> ports;
    int threadsNum;

    public ScannerInput(LinkedList<String> hosts, LinkedList<Integer> ports, int threadsNum){
        this.hosts = hosts;
        this.ports = ports;
        this.threadsNum = threadsNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScannerInput input = (ScannerInput) o;

        if(threadsNum != input.threadsNum || hosts.size() != input.hosts.size() || ports.size() != input.ports.size())
            return false;
        for(String host : hosts)
            if(!input.hosts.contains(host))
                return false;
        for(Integer port : ports)
            if(!input.ports.contains(port))
                return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hosts, ports, threadsNum);
    }

    @Override
    public String toString() {
        return "ScannerInput{" +
                "hosts=" + hosts +
                ", ports=" + ports +
                ", threadsNum=" + threadsNum +
                '}';
    }
}
