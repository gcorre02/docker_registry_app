package com.dockerapp.server.api.service.event;

import com.dockerapp.dao.entities.EventLogEntry;
import com.dockerapp.server.api.EventSearchRequest;
import org.springframework.data.domain.Page;

public interface EventLogService {
    Page<EventLogEntry> search(EventSearchRequest request);
}
