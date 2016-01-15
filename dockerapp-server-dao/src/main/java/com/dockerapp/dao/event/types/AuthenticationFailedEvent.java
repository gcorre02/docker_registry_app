package com.dockerapp.dao.event.types;

import com.dockerapp.dao.entities.EventLogEntry;
import com.dockerapp.dao.event.EventType;

public class AuthenticationFailedEvent extends AbstractEvent {
    private String username;

    public AuthenticationFailedEvent() {
        super(EventType.AUTHENTICATION_FAILED);
    }

    public void setPerformedById(String username) {
        this.username = username;
    }

    @Override
    public EventLogEntry toEventLogEntry() {
        EventLogEntry entry = new EventLogEntry();
        entry.setPerformedById(username);
        entry.setSuccess(false);
        return entry;
    }
}
