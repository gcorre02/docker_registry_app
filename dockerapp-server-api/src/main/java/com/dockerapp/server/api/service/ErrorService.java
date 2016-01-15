package com.dockerapp.server.api.service;

import com.dockerapp.dao.error.DockerAppError;

import java.util.List;

public interface ErrorService {

    List<String> getCauses(DockerAppError errorCode);

    List<String> getActions(DockerAppError errorCode);
}

