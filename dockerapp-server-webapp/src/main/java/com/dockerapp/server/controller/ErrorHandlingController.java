package com.dockerapp.server.controller;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.error.DockerAppException;
import com.dockerapp.dao.error.NotFoundException;
import com.dockerapp.dao.error.ValidationException;
import com.dockerapp.clientApi.ClientConnectionException;
import com.dockerapp.server.api.RequestContextHolder;
import com.dockerapp.server.api.security.ForbiddenException;
import com.dockerapp.server.api.security.UnauthorisedException;
import com.dockerapp.server.api.service.ErrorService;
import com.dockerapp.server.dto.ErrorResponse;
import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public abstract class ErrorHandlingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingController.class);
    private static final Map<Class<?>, HttpStatus> CLASS_TO_HTTP_STATUS = ImmutableMap.<Class<?>, HttpStatus>builder()
            .put(ClientConnectionException.class, HttpStatus.BAD_GATEWAY)
            .put(JsonParseException.class, HttpStatus.BAD_GATEWAY)
            .put(ValidationException.class, HttpStatus.BAD_REQUEST)
            .put(NotFoundException.class, HttpStatus.NOT_FOUND)
            .put(UnauthorisedException.class, HttpStatus.UNAUTHORIZED)
            .put(ForbiddenException.class, HttpStatus.FORBIDDEN)
            .put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST)
            .put(NullPointerException.class, HttpStatus.BAD_REQUEST)
            .put(IllegalStateException.class, HttpStatus.BAD_REQUEST)
            .build();

    @Autowired
    private ErrorService errorService;

    @ExceptionHandler(DockerAppException.class)
    @ResponseBody
    public ErrorResponse dockerappError(HttpServletRequest request, HttpServletResponse response, DockerAppException e) {
        return error(request, response, e, e.getError());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse internalServerError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return error(request, response, e, null);
    }

    protected ErrorResponse error(HttpServletRequest request, HttpServletResponse response, Exception e, DockerAppError errorCode) {
        HttpStatus status = getStatus(e);
        response.setStatus(getStatus(e).value());

        ErrorResponse error = new ErrorResponse(
                RequestContextHolder.get().getTransactionId(),
                status.getReasonPhrase(),
                errorCode.name(),
                e.getMessage(),
                errorService.getCauses(errorCode),
                errorService.getActions(errorCode));

        LOGGER.error(String.format("uuid=%s, type=%s, uri=%s, method=%s, parameters=%s, error=%s",
                RequestContextHolder.get().getTransactionId(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                request.getMethod(),
                request.getParameterMap(),
                e.getMessage()), e);
        return error;
    }

    private HttpStatus getStatus(Exception e) {
        HttpStatus status = CLASS_TO_HTTP_STATUS.get(e.getClass());
        return status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
    }
}