package com.dockerapp.server.dto.transformers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServerResponseHandler<T, R> {
    R respond(T object, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
