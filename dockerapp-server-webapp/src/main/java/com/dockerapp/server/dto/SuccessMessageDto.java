package com.dockerapp.server.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SuccessMessageDto {
    private final boolean success = true;
    private final String msg;

    public SuccessMessageDto(String message) {
        this.msg = message;
    }
}
