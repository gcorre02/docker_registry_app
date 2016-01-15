package com.dockerapp.server.api.security;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.error.DockerAppException;

public class ForbiddenException extends DockerAppException {
    public ForbiddenException(DockerAppError error) {
        super(error);
    }
    public ForbiddenException(DockerAppError error, String message) {
        super(error, message);
    }
}
