package com.dockerapp.dao.error;

public class DockerAppException extends RuntimeException {
    private final DockerAppError error;

    public DockerAppException(DockerAppError error) {
        this(error, error.getDescription());
    }

    public DockerAppException(DockerAppError error, String message) {
        super(message);
        this.error = error;
    }

    public DockerAppException(DockerAppError error, Throwable cause) {
        this(error, error.getDescription());
    }

    public DockerAppException(DockerAppError error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public DockerAppError getError() {
        return error;
    }
}
