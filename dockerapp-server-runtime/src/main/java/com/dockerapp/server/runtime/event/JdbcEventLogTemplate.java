package com.dockerapp.server.runtime.event;

import com.dockerapp.dao.entities.EventLogEntry;
import com.dockerapp.dao.error.DockerAppException;
import com.dockerapp.dao.event.Event;
import com.dockerapp.dao.event.EventLog;
import com.dockerapp.dao.event.EventLogTemplate;
import com.dockerapp.dao.repositories.EventLogEntryDao;
import com.dockerapp.server.api.RequestContextHolder;
import com.google.common.base.MoreObjects;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Component
public class JdbcEventLogTemplate implements EventLogTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcEventLogTemplate.class);
    private static final ThreadLocal<EventGroup> EVENT_CONTEXT = new ThreadLocal<>();
    private final EventBus eventBus = new AsyncEventBus("event-logging", Executors.newCachedThreadPool());

    @Autowired
    private EventLogEntryDao eventLogEntryDao;

    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    public JdbcEventLogTemplate() {
        eventBus.register(new EventLogWriter());
    }

    @Override
    public <E extends Event, R> R executeWithEventLog(EventLog<E, R> eventLog) {
        DateTime eventBegin = DateTime.now();
        Exception exception = null;
        E event = createEvent(eventLog.getClass());
        EventGroup context = getEventGroup(event);
        try {
            return eventLog.execute(event);
        } catch (Exception e) {
            exception = e;
        } finally {
            if (!event.ignoreEvent()) {
                context.add(event, populateEventContext(event, eventBegin, exception));
                if (context.isRootEvent(event)) {
                    EVENT_CONTEXT.remove();
                    publishEvent(context);
                }
            }
        }

        throw (exception instanceof RuntimeException)
                ? (RuntimeException) exception
                : new RuntimeException(exception);
    }

    private <E extends Event> EventGroup getEventGroup(E event) {
        EventGroup group = EVENT_CONTEXT.get();
        if (group == null) {
            group = new EventGroup(event);
            EVENT_CONTEXT.set(group);
        }
        return group;
    }

    private void publishEvent(EventGroup context) {
        eventBus.post(context);
    }

    private EventLogEntry populateEventContext(Event event, DateTime eventBegin, Exception exception) {
        RequestContextHolder context = RequestContextHolder.get();
        EventLogEntry entry = event.toEventLogEntry();
        entry.setEventTs(eventBegin);
        entry.setEventRuntime((int) (DateTime.now().getMillis() - eventBegin.getMillis()));
        entry.setEventType(event.getType());

        if (context.getIdentity() != null) {
            entry.setPerformedById(context.getIdentity().getIdentity());
            entry.setPerformedByRole(context.getIdentity().getRole().name());
        }

        entry.setRemoteIpAddress(context.getRemoteIpAddress());
        entry.setTransactionId(context.getTransactionId());
        entry.setSuccess(MoreObjects.firstNonNull(entry.isSuccess(), true));

        if (exception != null || event.getException() != null) {
            populateEventErrors(entry, exception == null ? event.getException() : exception);
        }

        return entry;
    }

    private void populateEventErrors(EventLogEntry entry, Exception e) {
        if (e instanceof DockerAppException) {
            entry.setFailureCode(((DockerAppException) e).getError());
        }
        entry.setFailureReason(e.getMessage());
        entry.setFailureType(e.getClass().getName());
        entry.setSuccess(false);
    }

    private <E extends Event, R> E createEvent(Class<?> clazz) {
        Type type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        try {
            //noinspection unchecked
            return (E) ((Class) type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private final class EventLogWriter {
        @Subscribe
        @AllowConcurrentEvents
        public void write(final EventGroup context) {
            try {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        EventLogEntry entry = eventLogEntryDao.save(context.rootEntry);
                        if (!context.childEventEntries.isEmpty()) {
                            for (EventLogEntry child : context.childEventEntries) {
                                child.setEventParentId(entry.getId());
                            }
                            eventLogEntryDao.save(context.childEventEntries);
                        }
                    }
                });
            } catch (Exception e) {
                LOGGER.error("error writing event log entry - FIX ME", e);
            }
        }
    }

    private final class EventGroup {
        private final Event rootEvent;
        private EventLogEntry rootEntry;
        private List<EventLogEntry> childEventEntries = new ArrayList<EventLogEntry>();

        public EventGroup(Event rootEvent) {
            this.rootEvent = rootEvent;
        }

        public void add(Event event, EventLogEntry entry) {
            if (isRootEvent(event)) {
                rootEntry = entry;
            } else {
                childEventEntries.add(entry);
            }
        }

        public boolean isRootEvent(Event event) {
            return event == rootEvent;
        }
    }
}
