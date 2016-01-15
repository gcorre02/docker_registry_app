package com.dockerapp.server.servlet;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;

public class InterceptingHttpServletRequest extends HttpServletRequestWrapper {

    private static final int MAX_PREFETCH = 1024;
    private final byte[] content = new byte[MAX_PREFETCH];
    private final int read;
    private final InputStream in;

    public InterceptingHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.read = ByteStreams.read(request.getInputStream(), content, 0, MAX_PREFETCH);
        in = new SequenceInputStream(
                new ByteArrayInputStream(content, 0, read),
                request.getInputStream());
    }

    public String getPreview() {
        return new String(content, 0, read);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return in.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(in));
    }
}
