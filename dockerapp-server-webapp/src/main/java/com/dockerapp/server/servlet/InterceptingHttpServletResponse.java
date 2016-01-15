package com.dockerapp.server.servlet;

import com.dockerapp.util.io.LimitedByteArrayOutputStream;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

public class InterceptingHttpServletResponse extends HttpServletResponseWrapper {
    private static final int MAX_PREFETCH = 1024;
    private final TeeOutputStream out;
    private final PrintWriter writer;
    private final LimitedByteArrayOutputStream previewStream;

    private final Multimap<String, String> headers = ArrayListMultimap.create();
    private HttpStatus status = HttpStatus.OK;
    private String statusMessage = HttpStatus.OK.getReasonPhrase();

    public InterceptingHttpServletResponse(HttpServletResponse response) throws IOException {
        super(response);
        previewStream = new LimitedByteArrayOutputStream(MAX_PREFETCH);
        out = new TeeOutputStream(response.getOutputStream(), previewStream);
        writer = new PrintWriter(out);
    }

    public String getPreview() throws IOException {
        out.flush();
        writer.flush();
        writer.flush();
        return previewStream.toString();
    }

    @Override
    public void setStatus(int sc) {
        status = HttpStatus.valueOf(sc);
        statusMessage = status.getReasonPhrase();
        super.setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        status = HttpStatus.valueOf(sc);
        statusMessage = sm;
        super.setStatus(sc, sm);
    }

    public int getStatus() {
        return status.value();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public void setHeader(String name, String value) {
        headers.removeAll(name);
        headers.put(name, value);
        super.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        headers.put(name, value);
        super.addHeader(name, value);
    }

    public Map<String, Collection<String>> getHeaders() {
        return headers.asMap();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                out.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }
}
