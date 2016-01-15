package com.dockerapp.clientApi;

import com.dockerapp.clientApi.entities.DockerRegistryRepositories;
import com.dockerapp.clientApi.entities.ImagesObj;

import java.io.IOException;
import java.util.Map;


public interface DockerRegistryClient {
    DockerRegistryRepositories getDockerRepos() throws IOException;

    Map<String, ImagesObj> getRepoImages(DockerRegistryRepositories repositories) throws IOException;
}
