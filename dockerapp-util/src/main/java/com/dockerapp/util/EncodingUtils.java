package com.dockerapp.util;

import javax.xml.bind.DatatypeConverter;

public final class EncodingUtils {

    private EncodingUtils() {

    }

    public static String fromBase64(String token) {
        return new String(DatatypeConverter.parseBase64Binary(token));
    }

    public static String toBase64(String token) {
        return DatatypeConverter.printBase64Binary(token.getBytes());
    }
}
