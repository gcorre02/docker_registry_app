package com.dockerapp.server.interceptor;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.server.api.RequestContextHolder;
import com.dockerapp.server.api.security.AuthIdentity;
import com.dockerapp.server.api.security.ForbiddenException;
import com.dockerapp.server.api.security.Permission;
import com.dockerapp.server.api.security.PermissionsAllowed;
import com.dockerapp.server.api.security.UnauthorisedException;
import com.dockerapp.server.api.service.AuthenticationService;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;

public class AuthorityInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityInterceptor.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
        //TODO [GR][12/10/15]  Use this with selenium tests!
        //TODO [GR][12/10/15]  remove this once the magento reset info issue is solved. (or find a better way of isolating this -> instead of environment, use SELENIUM=true variable
        if (request.getRequestURI().contains("migration")
                && !"production".equals(System.getProperty("environment"))
//                && "true".equalsIgnoreCase(System.getProperty("selenium"))) {
            System.out.println("is MIGRATUIN and is NOT PROD!!!!");
            return true;
        }
        */
        RequestContextHolder.get().setRemoteIpAddress(request.getRemoteAddr());
        AuthIdentity auth = authenticationService.authorised(request.getHeader(HttpHeaders.AUTHORIZATION));
        RequestContextHolder.get().setIdentity(auth);
        if (auth == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorisedException(DockerAppError.NOT_AUTHORISED);
        }

        Set<Permission> permissions = getRequiredPermissions(handler);
        if (!auth.hasPermissions(permissions)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            throw new ForbiddenException(DockerAppError.FORBIDDEN);
        }

        return true;
    }

    private Set<Permission> getRequiredPermissions(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            LOGGER.warn("expecting handler of type {}, but received {}", HandlerMethod.class.getName(), handler.getClass().getName());
            throw new IllegalArgumentException("unexpected handler type, check logs");
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PermissionsAllowed allowed = Objects.requireNonNull(
                handlerMethod.getMethodAnnotation(PermissionsAllowed.class),
                String.format("method %s.%s does not contain the %s annotation",
                        handlerMethod.getMethod().getDeclaringClass().getSimpleName(),
                        handlerMethod.getMethod().getName(),
                        PermissionsAllowed.class.getSimpleName()));

        return ImmutableSet.copyOf(allowed.value());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
