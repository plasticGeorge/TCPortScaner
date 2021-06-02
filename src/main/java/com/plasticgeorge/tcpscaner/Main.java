package com.plasticgeorge.tcpscaner;

import java.nio.file.Paths;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ArgumentsProcessor argumentsProcessor = new DefaultArgumentsProcessor();
        ScannerInput input = argumentsProcessor.process(args);
        PortsScanner scanner = new ThreadPoolScanner(input);
        Set<HostPortsStatus> res = scanner.scan();
        JsonSerializer<HostPortsStatus> serializer = new HostPortsStatusJsonSerializer();
        serializer.serialize(res, Paths.get("C:\\Users\\george\\IdeaProjects\\TCPortScanner\\ports.json"));
    }
}