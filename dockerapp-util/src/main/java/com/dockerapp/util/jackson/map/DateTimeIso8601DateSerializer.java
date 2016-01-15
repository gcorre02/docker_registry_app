package com.dockerapp.util.jackson.map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

public class DateTimeIso8601DateSerializer extends JsonSerializer<DateTime> {
    @Override
    public void serialize(DateTime value, JsonGenerator generator, SerializerProvider serializer) throws IOException {
        generator.writeString(value.toString());
    }
}