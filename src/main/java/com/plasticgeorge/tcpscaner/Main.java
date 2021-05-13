package com.plasticgeorge.tcpscaner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> hosts = new ArrayList<>();
        ArrayList<Integer> ports = new ArrayList<>();
        int threads = 4; // добавить многопоток

        //переделать циклы(какая та непонятная параша с 'i')
        for (int i = 0; i < args.length; ) {
            if (args[i].equals("-h")) {
                i++;
                for (; i < args.length && !args[i].startsWith("-"); i++) {
                    String[] a = args[i].replace(",", "").split("\\.");
                    if (a.length > 4)
                        throw new IllegalArgumentException("Invalid host ip - \'" + args[i] + "\'");
                    String[] b = a[0].split("-");
                    String[] c = a[1].split("-");
                    String[] d = a[2].split("-");
                    String[] e = a[3].split("-");
                    for (int j = Integer.parseInt(b[0]); j <= Integer.parseInt(b[b.length - 1]); j++)
                        for (int k = Integer.parseInt(c[0]); k <= Integer.parseInt(c[c.length - 1]); k++)
                            for (int l = Integer.parseInt(d[0]); l <= Integer.parseInt(d[d.length - 1]); l++)
                                for (int m = Integer.parseInt(e[0]); m <= Integer.parseInt(e[e.length - 1]); m++)
                                    hosts.add(j + "." + k + "." + l + "." + m);
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
                threads = Integer.parseInt(args[i++]);
            } else
                throw new IllegalArgumentException("Unknown parameter - \'" + args[i] + "\'");
        }
        Collections.shuffle(hosts);
        Collections.shuffle(ports);

        for (String host : hosts)
            for (int port : ports)
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 800);
                    System.out.println(socket.isConnected() ?
                            host + ":" + port + " - is open" :
                            host + ":" + port + " - is close");

                } catch (Exception e) {
                    System.out.println(host + ":" + port + " - is close");
                }
    }
}