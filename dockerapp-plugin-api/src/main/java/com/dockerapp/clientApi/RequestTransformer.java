package com.dockerapp.clientApi;

import java.io.IOException;
import java.util.Map;

public interface RequestTransformer<T, R> {
    R transform(T requestObject) throws IOException;

    String mapToString(T requestObject, Map<String, String> context) throws IOException;
}
