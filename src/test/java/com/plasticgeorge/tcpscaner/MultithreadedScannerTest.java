package com.plasticgeorge.tcpscaner;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class MultithreadedScannerTest {

    @Test
    public void scan() {
        TreeSet<HostPortsStatus> expected = new TreeSet<>();
        HashMap<Integer, String> tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "open");
        tempPortsStatus.put(443, "open");
        expected.add(new HostPortsStatus("64.233.162.102" ,tempPortsStatus));
        tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "open");
        tempPortsStatus.put(443, "open");
        expected.add(new HostPortsStatus("157.240.205.35" ,tempPortsStatus));
        tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "close");
        tempPortsStatus.put(443, "close");
        expected.add(new HostPortsStatus("0.10.10.0" ,tempPortsStatus));
        tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "close");
        tempPortsStatus.put(443, "close");
        expected.add(new HostPortsStatus("0.0.0.0" ,tempPortsStatus));

        LinkedList<String> hosts = new LinkedList<>();
        hosts.add("64.233.162.102"); //google.com
        hosts.add("157.240.205.35"); //facebook.com
        hosts.add("0.10.10.0"); //invalid host
        hosts.add("0.0.0.0"); //invalid host

        LinkedList<Integer> ports = new LinkedList<>();
        ports.add(53);
        ports.add(443);

        int threadsNum = 10;

        ScannerInput input = new ScannerInput(hosts, ports, threadsNum);

        PortsScanner scanner = new MultithreadedScanner(input);
        TreeSet<HostPortsStatus> actual = scanner.scan();

        Assert.assertEquals(expected, actual);
    }
}