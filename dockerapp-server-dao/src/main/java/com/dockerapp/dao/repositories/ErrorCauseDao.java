package com.dockerapp.dao.repositories;

import com.dockerapp.dao.error.ErrorCause;
import com.dockerapp.dao.error.DockerAppError;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ErrorCauseDao extends CrudRepository<ErrorCause, Long>, JpaSpecificationExecutor<ErrorCause> {
    List<ErrorCause> findByErrorCodeOrderByOrderHintAsc(DockerAppError errorCode);
}
