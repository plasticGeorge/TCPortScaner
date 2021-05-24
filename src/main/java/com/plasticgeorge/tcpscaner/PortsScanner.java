package com.plasticgeorge.tcpscaner;

import java.util.TreeSet;

public interface PortsScanner {
    TreeSet<HostPortsStatus> scan();
}
