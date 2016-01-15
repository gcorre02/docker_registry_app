package com.dockerapp.dao.repositories;

import com.dockerapp.dao.entities.EventLogEntry;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface EventLogEntryDao extends CrudRepository<EventLogEntry, Long>, JpaSpecificationExecutor<EventLogEntry> {
}
