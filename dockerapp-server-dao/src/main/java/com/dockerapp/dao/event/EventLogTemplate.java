package com.dockerapp.dao.event;

public interface EventLogTemplate {
    <E extends Event, R> R executeWithEventLog(EventLog<E, R> eventLog) throws RuntimeException;
}
