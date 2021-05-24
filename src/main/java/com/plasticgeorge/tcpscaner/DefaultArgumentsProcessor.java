package com.plasticgeorge.tcpscaner;

import java.util.Collections;
import java.util.LinkedList;

public class DefaultArgumentsProcessor implements ArgumentsProcessor{
    @Override
    public ScannerInput process(String[] args) {
        LinkedList<String> hosts = new LinkedList<>();
        LinkedList<Integer> ports = new LinkedList<>();
        int threads = 4;

        for (int i = 0; i < args.length; ) {
            if (args[i].equals("-h")) {
                i++;
                for (; i < args.length && !args[i].startsWith("-"); i++) {
                    String[] a = args[i].replace(",", "").split("\\.");
                    if (a.length != 4)
                        throw new IllegalArgumentException("Invalid host ip - \'" + args[i] + "\'");
                    String[] b = a[0].split("-");
                    String[] c = a[1].split("-");
                    String[] d = a[2].split("-");
                    String[] e = a[3].split("-");
                    for (int j = Integer.parseInt(b[0]); j <= Integer.parseInt(b[b.length - 1]); j++)
                        for (int k = Integer.parseInt(c[0]); k <= Integer.parseInt(c[c.length - 1]); k++)
                            for (int l = Integer.parseInt(d[0]); l <= Integer.parseInt(d[d.length - 1]); l++)
                                for (int m = Integer.parseInt(e[0]); m <= Integer.parseInt(e[e.length - 1]); m++) {
                                    if(j < 0 || j > 255
                                    || k < 0 || k > 255
                                    || l < 0 || l > 255
                                    || m < 0 || m > 255)
                                        throw new IllegalArgumentException("Incorrect host(s) entered  - \'" + args[i] + "\'");
                                    hosts.push(j + "." + k + "." + l + "." + m);
                                }
                }
            } else if (args[i].equals("-p")) {
                i++;
                for (; i < args.length && !args[i].startsWith("-"); i++) {
                    String[] a = args[i].replace(",", "").split("-");
                    for (int j = Integer.parseInt(a[0]); j <= Integer.parseInt(a[a.length - 1]); j++)
                        ports.add(j);
                }
            } else if (args[i].equals("-t")) {
                i++;
                int temp = Integer.parseInt(args[i++]);
                if(temp <= 0)
                    throw new IllegalArgumentException("Incorrect number of threads entered  - \'" + temp + "\'");
                threads = temp;
            } else
                throw new IllegalArgumentException("Unknown parameter - \'" + args[i] + "\'");
        }

        Collections.shuffle(hosts);
        Collections.shuffle(ports);

        return new ScannerInput(hosts, ports, threads);
    }
}
