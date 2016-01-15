package com.dockerapp.dao.event.types;

import com.dockerapp.dao.event.Event;
import com.dockerapp.dao.event.EventType;

public abstract class AbstractEvent implements Event {

    private final EventType type;
    private boolean ignoreEvent;
    private Exception exception;

    public AbstractEvent(EventType type) {
        this.type = type;
    }

    @Override
    public final EventType getType() {
        return type;
    }

    @Override
    public boolean ignoreEvent() {
        return ignoreEvent;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    protected void setIgnoreEvent(boolean ignoreEvent) {
        this.ignoreEvent = ignoreEvent;
    }

    protected <T> T firstNonNull(T value1, T value2) {
        return value1 == null ? value2 : value1;
    }
}
