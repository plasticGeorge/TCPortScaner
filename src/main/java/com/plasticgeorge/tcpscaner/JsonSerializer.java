package com.plasticgeorge.tcpscaner;

import java.nio.file.Path;
import java.util.Collection;

public interface JsonSerializer<T> {
    void serialize(Collection<T> data, Path pathToFile);
}
