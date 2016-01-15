package com.dockerapp.server.api;

import com.dockerapp.server.api.security.AuthIdentity;

import java.util.UUID;

public class RequestContextHolder {
    private static final ThreadLocal<RequestContextHolder> THREAD_LOCAL = new ThreadLocal<>();

    private final String transactionId = UUID.randomUUID().toString();
    private AuthIdentity identity;
    private String remoteIpAddress;

    public static RequestContextHolder get() {
        RequestContextHolder context = THREAD_LOCAL.get();
        if (context == null) {
            context = new RequestContextHolder();
            THREAD_LOCAL.set(context);
        }
        return THREAD_LOCAL.get();
    }

    public static RequestContextHolder clear() {
        RequestContextHolder context = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        return context;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public AuthIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(AuthIdentity identity) {
        this.identity = identity;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }
}
