package com.dockerapp.pluginapi.http;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class UriTokenMapperTest {
    private UriTokenMapper mapper = new UriTokenMapper();

    @Test
    public void testNoValuesToSubstitute() {
        String response = mapper.map("/query", Collections.<String, String>emptyMap());
        assertEquals("/query", response);
    }

    @Test
    public void testSingleValueToSubstitute() {
        String response = mapper.map("/query?q={query}", ImmutableMap.of("query", "whoami"));
        assertEquals("/query?q=whoami", response);
    }

    @Test
    public void testSingleValueMultipleOccurencesToSubstitute() {
        String response = mapper.map("/query?q={query}&p={query}", ImmutableMap.of("query", "whoami"));
        assertEquals("/query?q=whoami&p=whoami", response);
    }

    @Test
    public void testMultipleValuesToSubstitute() {
        String response = mapper.map("/query?q={query}&p={page}", ImmutableMap.of(
                "query", "whoami",
                "page", "1"));
        assertEquals("/query?q=whoami&p=1", response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionThrownIfValueMissing() {
        mapper.map("/query?q={query}", Collections.<String, String>emptyMap());
    }

}
