package com.dockerapp.server.api.service.docker;


import com.dockerapp.server.api.docker.DockerResponse;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by root on 8/12/15.
 */
public interface DockerService {
    @Transactional
    DockerResponse getRegistryDetails() throws IOException;
}
