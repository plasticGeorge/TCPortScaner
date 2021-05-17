package com.plasticgeorge.tcpscaner;

public class Main {
    public static void main(String[] args) {
        ArgumentsProcessor processor = new DefaultArgumentsProcessor();
        PortsScanner scanner = new MultithreadedScanner(processor.process(args));
        scanner.scan();
    }
}