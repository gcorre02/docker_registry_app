package com.dockerapp.server.runtime;

import com.dockerapp.dao.error.ErrorAction;
import com.dockerapp.dao.error.ErrorCause;
import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.repositories.ErrorActionDao;
import com.dockerapp.dao.repositories.ErrorCauseDao;
import com.dockerapp.server.api.service.ErrorService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ErrorServiceImpl implements ErrorService {
    @Autowired
    private ErrorCauseDao errorCauseDao;

    @Autowired
    private ErrorActionDao errorActionDao;

    @Override
    @Transactional(readOnly = true)
    public List<String> getCauses(DockerAppError errorCode) {
        return Lists.transform(errorCauseDao.findByErrorCodeOrderByOrderHintAsc(errorCode), new Function<ErrorCause, String>() {
            @Override
            public String apply(ErrorCause input) {
                return input.getErrorCause();
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getActions(DockerAppError errorCode) {
        return Lists.transform(errorActionDao.findByErrorCodeOrderByOrderHintAsc(errorCode), new Function<ErrorAction, String>() {
            @Override
            public String apply(ErrorAction input) {
                return input.getErrorAction();
            }
        });
    }
}
