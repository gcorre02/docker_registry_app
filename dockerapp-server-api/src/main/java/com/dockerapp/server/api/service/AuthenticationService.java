package com.dockerapp.server.api.service;

import com.dockerapp.server.api.security.AuthIdentity;

public interface AuthenticationService {
    AuthIdentity getIdentity();

    AuthIdentity authorised(String authorisationHeader);
}
