package com.plasticgeorge.tcpscaner;

import java.util.Set;

public interface PortsScanner {
    Set<HostPortsStatus> scan();
}
