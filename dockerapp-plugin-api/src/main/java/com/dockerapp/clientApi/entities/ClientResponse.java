package com.dockerapp.clientApi.entities;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Model class used for the communication between servlet and the modules.
 * */

public class ClientResponse {
    private final String body;
    private final Map<String, List<String>> headers;
    private final int status;

    public ClientResponse(int status, Map<String, List<String>> headers, String body) {
        this.status = status;
        this.headers = headers == null ? null : Collections.unmodifiableMap(headers);
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
