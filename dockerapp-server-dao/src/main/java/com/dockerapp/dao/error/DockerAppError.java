package com.dockerapp.dao.error;

public enum DockerAppError {
    FORBIDDEN("Access denied for this resource"),
    MISSING_FIELD("Required field is missing"),
    NOT_AUTHORISED("Invalid access credentials"),
    REMOTE_CLIENT_COMMUNICATION_ERROR("Error communicating with remote merchant"),
    SCHEDULER_ERROR("An error occured configuring the scheduler"),
    VALIDATION_FAILED("Field did not pass validation checks");

    private final String description;

    private DockerAppError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
