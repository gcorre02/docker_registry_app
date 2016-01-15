package com.dockerapp.dao.repositories;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.error.ErrorAction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ErrorActionDao extends CrudRepository<ErrorAction, Long>, JpaSpecificationExecutor<ErrorAction> {
    List<ErrorAction> findByErrorCodeOrderByOrderHintAsc(DockerAppError errorCode);
}
