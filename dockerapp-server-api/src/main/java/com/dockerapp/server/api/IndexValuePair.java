package com.dockerapp.server.api;

//TODO:[GR][14/9/15]Generalize this object and use it for all index/value pairs.
public class IndexValuePair {
    private Long index;
    private String value;

    public Long getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "index=" + index +
                ", value='" + value + '\'' +
                '}';
    }
}

