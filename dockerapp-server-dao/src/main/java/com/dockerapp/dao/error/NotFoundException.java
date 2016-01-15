package com.dockerapp.dao.error;

public class NotFoundException extends DockerAppException {
    public NotFoundException(DockerAppError error) {
        super(error);
    }
    public NotFoundException(DockerAppError error, String message) {
        super(error, message);
    }
}
