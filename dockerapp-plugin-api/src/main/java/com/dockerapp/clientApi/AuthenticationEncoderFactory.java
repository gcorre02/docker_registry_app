package com.dockerapp.clientApi;

import com.dockerapp.dao.entities.AuthenticationMethod;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationEncoderFactory {

    private final Map<AuthenticationMethod, AuthenticationEncoder> encoders = new HashMap<>();

    public AuthenticationEncoderFactory() {
        register(AuthenticationMethod.ENCODE_64, new Base64Encoder());
        register(AuthenticationMethod.REQUEST_HASH, new RequestHashEncoder());
    }

    public void register(AuthenticationMethod method, AuthenticationEncoder encoder) {
        encoders.put(method, encoder);
    }

    public AuthenticationEncoder get(AuthenticationMethod method) throws UnsupportedEncodingException {
        AuthenticationEncoder encoder = encoders.get(method);
        if (encoder == null) {
            throw new UnsupportedEncodingException(method.name());
        }
        return encoder;
    }


    private static class Base64Encoder implements AuthenticationEncoder {
        @Override
        public String encode(String password) {
            return DatatypeConverter.printBase64Binary(password.getBytes());
        }
    }

    private static class RequestHashEncoder implements AuthenticationEncoder {
        @Override
        public String encode(String password) {
            throw new UnsupportedOperationException();
        }
    }
}
