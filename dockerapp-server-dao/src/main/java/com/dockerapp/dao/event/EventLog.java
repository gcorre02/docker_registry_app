package com.dockerapp.dao.event;

public interface EventLog<E extends Event, R> {
    R execute(E event) throws Exception;
}
