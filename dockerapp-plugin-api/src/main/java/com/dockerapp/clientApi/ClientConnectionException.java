package com.dockerapp.clientApi;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.error.DockerAppException;

public class ClientConnectionException extends DockerAppException {
    public ClientConnectionException(DockerAppError error) {
        super(error);
    }

    public ClientConnectionException(DockerAppError error, String message) {
        super(error, message);
    }

    public ClientConnectionException(DockerAppError error, Throwable cause) {
        super(error, cause);
    }

    public ClientConnectionException(DockerAppError error, String message, Throwable cause) {
        super(error, message, cause);
    }
}
