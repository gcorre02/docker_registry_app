package com.dockerapp.clientApi;

public interface AuthenticationEncoder {
    String encode(String secretKey);
}
