package com.dockerapp.server.interceptor;

import com.dockerapp.server.api.RequestContextHolder;
import com.dockerapp.server.servlet.InterceptingHttpServletRequest;
import com.dockerapp.server.servlet.InterceptingHttpServletResponse;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
//TODO: [GR][11/7/15] Review log() overloading and refactor for code efficiency (centralise String Builders)
public class TransactionLoggerInterceptor implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLoggerInterceptor.class);
    private static final int LABEL_MIN_WIDTH = 20;
    private static final int ERROR_MARGIN = 300;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String transactionUuid = RequestContextHolder.get().getTransactionId();

        InterceptingHttpServletRequest servletRequest = new InterceptingHttpServletRequest((HttpServletRequest) request);
        InterceptingHttpServletResponse servletResponse = new InterceptingHttpServletResponse((HttpServletResponse) response);

        if (LOGGER.isDebugEnabled()) {
            log(transactionUuid, servletRequest, false);
        }

        Exception unhandledException = null;
        try {
            chain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            unhandledException = e;
            throw e;
        } finally {
            if (LOGGER.isDebugEnabled()) {
                log(transactionUuid, servletRequest, servletResponse, unhandledException);
            } else if (isErrorStatus(servletResponse)) {
                log(transactionUuid, servletRequest, true);
                log(transactionUuid, servletRequest, servletResponse, unhandledException);
            }
            RequestContextHolder.clear();
        }
    }

    private void log(
            String transactionUuid,
            InterceptingHttpServletRequest request,
            InterceptingHttpServletResponse response,
            Exception unhandledException) {

        StringBuilder sb = new StringBuilder();
        appendBoundary(transactionUuid, sb, '<', true);

        sb.append(request.getProtocol()).append(" ")
                .append(response.getStatus()).append(" ")
                .append(response.getStatusMessage()).append("\n");

        sb.append(Strings.padEnd("Identity: ", LABEL_MIN_WIDTH, ' ')).append(RequestContextHolder.get().getIdentity()).append("\n");

        for (Map.Entry<String, Collection<String>> headers : response.getHeaders().entrySet()) {
            String name = headers.getKey();
            for (String value : headers.getValue()) {
                sb.append(Strings.padEnd(prettyHeader(name) + ": ", LABEL_MIN_WIDTH, ' ')).append(value).append("\n");
            }
        }

        try {
            appendBody(sb, response.getPreview());
        } catch (IOException e) {
            appendBody(sb, "exception writing body");
        }
        appendBoundary(transactionUuid, sb, '<', false);
        if (unhandledException != null) {
            sb.append("\n").append(Strings.padEnd("Uncaught Exception:", LABEL_MIN_WIDTH, ' '))
                    .append(unhandledException.getClass().getName())
                    .append(":").append(unhandledException.getMessage());
        }

        if (isErrorStatus(response) || unhandledException != null) {
            LOGGER.error(sb.toString(), unhandledException);
        } else {
            LOGGER.debug(sb.toString());
        }
    }

    private void log(String transactionUuid, InterceptingHttpServletRequest request, boolean logAsError) {
        StringBuilder sb = new StringBuilder();
        appendBoundary(transactionUuid, sb, '>', true);
        sb.append(request.getMethod()).append(" ")
                .append(request.getRequestURI())
                .append(Strings.isNullOrEmpty(request.getQueryString()) ? "" : "?").append(Strings.nullToEmpty(request.getQueryString()))
                .append(" ").append(request.getProtocol()).append("\n");

        sb.append(Strings.padEnd("Remote-Address: ", LABEL_MIN_WIDTH, ' ')).append(request.getRemoteAddr()).append("\n");

        for (Enumeration names = request.getHeaderNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            for (Enumeration values = request.getHeaders(name); values.hasMoreElements();) {
                String value = (String) values.nextElement();
                if (name.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)) {
                    value = "[REDACTED]";
                }
                sb.append(Strings.padEnd(prettyHeader(name) + ": ", LABEL_MIN_WIDTH, ' ')).append(value).append("\n");
            }
        }

        appendBody(sb, request.getPreview());
        appendBoundary(transactionUuid, sb, '>', false);

        if (logAsError) {
            LOGGER.error(sb.toString());
        } else {
            LOGGER.debug(sb.toString());
        }
    }

    private void appendBody(StringBuilder sb, String body) {
        sb.append(Strings.padEnd("Body: ", LABEL_MIN_WIDTH, ' ')).append(Strings.isNullOrEmpty(body) ? "<NO-BODY>" : body);
    }

    private StringBuilder appendBoundary(String transactionUuid, StringBuilder sb, char identifier, boolean start) {
        sb.append("\n")
                .append(Strings.padStart(" ", LABEL_MIN_WIDTH, identifier))
                .append(start ? "BEGIN" : "END")
                .append(" ").append(transactionUuid).append("\n");
        return sb;
    }

    private String prettyHeader(String header) {
        return WordUtils.capitalize(header, ' ', '-');
    }

    private boolean isErrorStatus(InterceptingHttpServletResponse response) {
        return response.getStatus() >= ERROR_MARGIN;
    }
}
