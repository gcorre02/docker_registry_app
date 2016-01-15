package com.dockerapp.dao.util;

import com.dockerapp.dao.error.Check;
import com.dockerapp.dao.error.DockerAppError;
import com.google.common.base.Strings;

public final class Builders {
    private Builders() {

    }

    public static void len(String value, String fieldName, int maxLength) {
        if (Strings.isNullOrEmpty(value)) {
            return;
        }
        Check.validate(value.length() <= maxLength, DockerAppError.VALIDATION_FAILED, String.format(
                "field [%s] max allowed length is [%s], actual length is [%s]",
                fieldName, maxLength, value.length()));
    }
    public static void lenBetween(String value, String fieldName, int minLength, int maxLength) {
        if (Strings.isNullOrEmpty(value)) {
            return;
        }
        Check.validate(minLength <= value.length() && value.length() <= maxLength, DockerAppError.VALIDATION_FAILED, String.format(
                "field [%s] allowed length is between [%s] and [%s], actual length is [%s]",
                fieldName, minLength, maxLength, value.length()));
    }

    public static void pattern(String value, String fieldName, String pattern) {
        if (Strings.isNullOrEmpty(value)) {
            return;
        }
        Check.validate(value.matches(pattern), DockerAppError.VALIDATION_FAILED, String.format(
                "field [%s] value is invalid, should match pattern [%s]",
                fieldName, pattern));
    }

    public static String required(String value, String fieldName) {
        Check.validate(!Strings.isNullOrEmpty(value), DockerAppError.MISSING_FIELD, String.format(
                "field [%s] is required",
                fieldName));
        return value;
    }

    public static <T> T required(T value, String fieldName) {
        Check.validate(value != null, DockerAppError.MISSING_FIELD, String.format(
                "field [%s] is required",
                fieldName));
        return value;
    }
}
