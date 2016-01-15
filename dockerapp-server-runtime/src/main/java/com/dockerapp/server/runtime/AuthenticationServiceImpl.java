package com.dockerapp.server.runtime;

import com.dockerapp.dao.event.EventLog;
import com.dockerapp.dao.event.EventLogTemplate;
import com.dockerapp.dao.event.types.AuthenticationFailedEvent;
import com.dockerapp.server.api.RequestContextHolder;
import com.dockerapp.server.api.security.AuthIdentity;
import com.dockerapp.server.api.security.Role;
import com.dockerapp.server.api.service.AuthenticationService;
import com.dockerapp.util.EncodingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final String ADMIN_USER_NAME = "dockerapp";
    private static final String ADMIN_USER_PASSWORD = "secretPassword";
    private static final Pattern BASIC_SCHEME_PATTERN = Pattern.compile("^\\s*Basic\\s*(?<token>.+)\\s*$");

    @Autowired
    private EventLogTemplate eventLogTemplate;

    @Override
    public AuthIdentity getIdentity() {
        return RequestContextHolder.get().getIdentity();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthIdentity authorised(String authorisationHeader) {
        if (authorisationHeader == null) {
            return new AuthIdentity(null, Role.UNAUTHENTICATED);
        }

        Matcher matcher = BASIC_SCHEME_PATTERN.matcher(authorisationHeader);
        if (!matcher.find()) {
            return null;
        }

        String[] tokens = EncodingUtils.fromBase64(matcher.group("token")).split(":");
        if (tokens.length != 2) {
            return null;
        }

        String username = tokens[0];
        String password = tokens[1];
        return authoriseUser(username, password);
    }

    private AuthIdentity authoriseUser(String username, String password) {
        return ADMIN_USER_NAME.equals(username)
                ? checkPasswordForIdentity(username, ADMIN_USER_PASSWORD, password, Role.DOCKERAPP_ADMIN)
                : null;
    }


    private AuthIdentity checkPasswordForIdentity(String username, String identityPassword, String authPassword, Role role) {
        return identityPassword.equals(authPassword)
                ? new AuthIdentity(username, role)
                : eventLogFailure(username);
    }

    private AuthIdentity eventLogFailure(final String username) {
        eventLogTemplate.executeWithEventLog(new EventLog<AuthenticationFailedEvent, Class<Void>>() {
            @Override
            public Class<Void> execute(AuthenticationFailedEvent event) throws Exception {
                event.setPerformedById(username);
                return Void.TYPE;
            }
        });
        return null;
    }
}
