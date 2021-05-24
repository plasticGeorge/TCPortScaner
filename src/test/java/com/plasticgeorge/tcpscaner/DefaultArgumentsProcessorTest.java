package com.plasticgeorge.tcpscaner;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class DefaultArgumentsProcessorTest {

    @Test
    public void process1() {
        LinkedList<String> hosts = new LinkedList<>();
        hosts.add("8.8.8.8");
        hosts.add("8.8.8.9");
        hosts.add("8.8.8.10");
        hosts.add("192.168.1.1");
        hosts.add("162.23.87.219");

        LinkedList<Integer> ports = new LinkedList<>();
        ports.add(53);
        ports.add(443);
        ports.add(444);
        ports.add(445);
        ports.add(999);

        int threadsNum = 10;

        ScannerInput expected = new ScannerInput(hosts, ports, threadsNum);

        String[] arguments = {"-h", "8.8.8.8-10,", "192.168.1.1",
                "-p", "53,", "443-445",
                "-t", "8",
                "-p", "999",
                "-h", "162.23.87.219",
                "-t", "10"};

        ScannerInput actual = new DefaultArgumentsProcessor().process(arguments);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void process2(){
        String[] arguments = {"-h", "8.8.8.8-10,", "192.168.1.1",
                "-p", "53,", "443-445",
                "-t", "-2",
                "-p", "999",
                "-h", "162.23.87.219",
                "-t", "-1"};

        try {
            ScannerInput actual = new DefaultArgumentsProcessor().process(arguments);
        }
        catch (IllegalArgumentException e){
            Assert.assertEquals(e.getMessage(), "Incorrect number of threads entered  - \'-2\'");
        }
    }

    @Test
    public void process3(){
        String[] arguments = {"-h", "8.8.8.8-10,", "192.168.1.1",
                "-p", "53,", "443-445",
                "-t", "4",
                "-p", "999",
                "-h", "999.999.999.999", //!!!
                "-t", "4"};

        try {
            ScannerInput actual = new DefaultArgumentsProcessor().process(arguments);
        }
        catch (IllegalArgumentException e){
            Assert.assertEquals(e.getMessage(), "Incorrect host(s) entered  - \'999.999.999.999\'");
        }
    }
}