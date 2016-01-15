package com.dockerapp.server.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ErrorResponse {
    private final String id;
    private final String message;
    private final String code;
    private final String description;
    private final List<String> causes;
    private final List<String> actions;

    public ErrorResponse(String transactionUuid, String message, String code, String description, List<String> causes, List<String> actions) {
        this.id = transactionUuid;
        this.message = message;
        this.causes = causes;
        this.actions = actions;
        this.code = code;
        this.description = description;
    }

    public String toString() {
        return String.format("Error[id=%s, message=%s, code=%s, description=%s, causes=%s, actions=%s]", id, message, code, description, causes, actions);
    }
}