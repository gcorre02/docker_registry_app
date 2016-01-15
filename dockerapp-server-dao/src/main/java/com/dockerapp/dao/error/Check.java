package com.dockerapp.dao.error;

public final class Check {
    private Check() {

    }

    public static <T> T validateNotNull(T object, DockerAppError error) {
        validate(object != null, error, error.getDescription());
        return object;
    }

    public static void validate(boolean condition, DockerAppError error) {
        validate(condition, error, error.getDescription());
    }

    public static void validate(boolean condition, DockerAppError error, String message) {
        if (!condition) {
            throw new ValidationException(error, message == null ? error.getDescription() : message);
        }
    }

    public static <T> T requiresNonNull(T object, DockerAppError error) {
        return requiresNonNull(object, error, error.getDescription());
    }

    public static <T> T requiresNonNull(T object, DockerAppError error, String message) {
        if (object == null) {
            throw new NotFoundException(error, message);
        }
        return object;
    }

}
