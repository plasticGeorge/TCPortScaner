package com.plasticgeorge.tcpscaner;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public class DefaultJsonSerializer implements JsonSerializer<HostPortsStatus>{

    @Override
    public void serialize(Collection<HostPortsStatus> data, Path pathToFile) {
        JSONObject preparedObject = new JSONObject();
        for(HostPortsStatus elem : data){
            preparedObject.put(elem.host, new JSONObject(elem.portStatus));
        }
        try (FileWriter file = new FileWriter(pathToFile.toString())){
            file.write(preparedObject.toJSONString());
            file.flush();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
