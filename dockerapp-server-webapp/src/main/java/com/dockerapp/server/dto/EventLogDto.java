package com.dockerapp.server.dto;

import com.dockerapp.dao.entities.EventLogEntry;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class EventLogDto {

    private final long id;
    private final Map<String, String> eventColumns;

    private EventLogDto(long id, Map<String, String> eventColumns) {
        this.id = id;
        this.eventColumns = eventColumns;
    }

    public static List<EventLogDto> build(List<EventLogEntry> content) {
        List<EventLogDto> events = new ArrayList<>(content.size());
        for (EventLogEntry entry : content) {
            events.add(new EventLogDto(entry.getId(), entry.toMap()));
        }
        return events;
    }
}
