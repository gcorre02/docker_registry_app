package com.dockerapp.server.api.security;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.error.DockerAppException;

public class UnauthorisedException extends DockerAppException {
    public UnauthorisedException(DockerAppError error) {
        super(error);
    }
    public UnauthorisedException(DockerAppError error, String message) {
        super(error, message);
    }
}
