package com.plasticgeorge.tcpscaner;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class HostPortsStatusJsonSerializerTest {

    @Test
    public void serialize() {
        TreeSet<HostPortsStatus> objectForSerialize = new TreeSet<>();
        HashMap<Integer, String> tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "open");
        tempPortsStatus.put(443, "open");
        objectForSerialize.add(new HostPortsStatus("64.233.162.102" ,tempPortsStatus));
        tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "open");
        tempPortsStatus.put(443, "open");
        objectForSerialize.add(new HostPortsStatus("157.240.205.35" ,tempPortsStatus));
        tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "close");
        tempPortsStatus.put(443, "close");
        objectForSerialize.add(new HostPortsStatus("0.10.10.0" ,tempPortsStatus));
        tempPortsStatus = new HashMap<>();
        tempPortsStatus.put(53, "close");
        tempPortsStatus.put(443, "close");
        objectForSerialize.add(new HostPortsStatus("0.0.0.0" ,tempPortsStatus));

        JsonSerializer<HostPortsStatus> serializer = new HostPortsStatusJsonSerializer();
        serializer.serialize(objectForSerialize, System.getProperty("user.dir") + "\\src\\test\\resources\\actual.json");

        StringBuilder actual  = new StringBuilder();

        try(Stream<String> stream = Files.lines(Paths.get(System.getProperty("user.dir") + "\\src\\test\\resources\\actual.json"))){
            stream.forEach(actual::append);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        String expected = "{\"0.0.0.0\":{\"53\":\"close\",\"443\":\"close\"},\"64.233.162.102\":{\"53\":\"open\",\"443\":\"open\"},\"0.10.10.0\":{\"53\":\"close\",\"443\":\"close\"},\"157.240.205.35\":{\"53\":\"open\",\"443\":\"open\"}}";

        Assert.assertEquals(expected, actual.toString());
    }
}