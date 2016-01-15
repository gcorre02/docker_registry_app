package com.dockerapp.server.api.docker;

import com.dockerapp.clientApi.entities.ImagesObj;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.Map;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DockerResponse {
    private final Map<String, ImagesObj> repositories;

    public DockerResponse(Map<String, ImagesObj> repositories) {
        this.repositories = repositories;
    }

    public Map<String, ImagesObj> getRepositories() {
        return repositories;
    }
}