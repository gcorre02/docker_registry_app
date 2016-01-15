package com.dockerapp.dao.transaction;

public final class TransactionTimeout {
    public static final int STANDARD = 60;
    public static final int LONG = 60 * 10;

    private TransactionTimeout() {

    }
}
