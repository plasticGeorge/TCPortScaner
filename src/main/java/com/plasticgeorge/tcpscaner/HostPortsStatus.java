package com.plasticgeorge.tcpscaner;

import java.util.HashMap;

public class HostPortsStatus implements Comparable<HostPortsStatus> {
    String host;
    HashMap<Integer, String> portStatus;

    public HostPortsStatus(String host, HashMap<Integer, String> portStatus) {
        this.host = host;
        this.portStatus = portStatus;
    }

    @Override
    public int compareTo(HostPortsStatus o) {
        return host.compareTo(o.host);
    }

    @Override
    public String toString() {
        return "HostPortStatus{" +
                "host='" + host + '\'' +
                ", portStatus=" + portStatus +
                '}';
    }
}
