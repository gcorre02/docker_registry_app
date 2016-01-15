package com.dockerapp.server.runtime.docker;

import com.dockerapp.clientApi.DockerRegistryClient;
import com.dockerapp.clientApi.entities.DockerRegistryRepositories;
import com.dockerapp.server.api.docker.DockerResponse;
import com.dockerapp.server.api.service.docker.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Component
public class DockerServiceImpl implements DockerService {
    @Autowired
    private DockerRegistryClient dockerRegistryClient;

    @Transactional
    @Override
    public DockerResponse getRegistryDetails() throws IOException {
        DockerRegistryRepositories repositories = dockerRegistryClient.getDockerRepos();
        System.out.println(">>>>>>>>>>>>>>>>>> Success! in getting repos...");
        DockerResponse response = new DockerResponse(dockerRegistryClient.getRepoImages(repositories));
        return response;
    }
}
