package com.dockerapp.clientApi;

import java.io.IOException;

public interface ResponseTransformer<T, R> {
    T transformResponse(R response) throws IOException;
}
