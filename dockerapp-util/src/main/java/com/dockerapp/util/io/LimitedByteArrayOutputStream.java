package com.dockerapp.util.io;

import java.io.IOException;
import java.io.OutputStream;

public class LimitedByteArrayOutputStream extends OutputStream {
    private final byte[] buffer;
    private int offset;

    public LimitedByteArrayOutputStream(int limit) {
        this.buffer = new byte[limit];
    }

    @Override
    public void write(int b) throws IOException {
        if (offset < buffer.length) {
            buffer[offset++] = (byte) b;
        }
    }

    @Override
    public String toString() {
        return new String(buffer, 0, offset);
    }
}
