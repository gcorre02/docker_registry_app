package com.dockerapp.dao.error;

public class ValidationException extends DockerAppException {
    public ValidationException(DockerAppError error) {
        super(error);
    }
    public ValidationException(DockerAppError error, String message) {
        super(error, message);
    }
}
