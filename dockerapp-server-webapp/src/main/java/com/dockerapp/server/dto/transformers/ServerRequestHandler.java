package com.dockerapp.server.dto.transformers;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ServerRequestHandler<T> {
    T toRequestObject(HttpServletRequest request) throws IOException;
}
