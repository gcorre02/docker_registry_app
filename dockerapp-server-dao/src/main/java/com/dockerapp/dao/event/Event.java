package com.dockerapp.dao.event;

import com.dockerapp.dao.entities.EventLogEntry;

public interface Event {
    EventType getType();
    EventLogEntry toEventLogEntry();
    boolean ignoreEvent();
    Exception getException();
}
