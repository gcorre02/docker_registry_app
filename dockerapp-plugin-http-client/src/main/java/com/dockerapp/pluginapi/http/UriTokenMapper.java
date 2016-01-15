package com.dockerapp.pluginapi.http;

import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UriTokenMapper {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{([^}]*)\\}");

    public String map(String uri, Map<String, String> values) {
        Set<String> tokens = findTokens(uri);
        for (String token : tokens) {
            Preconditions.checkArgument(values.containsKey(token), String.format("could not map uri due to missing token value [%s]", token));
            uri = uri.replaceAll(Pattern.quote("{" + token + "}"), values.get(token));
        }
        return uri;
    }

    private Set<String> findTokens(String uri) {
        Set<String> tokens = new HashSet<String>();
        Matcher matcher = TOKEN_PATTERN.matcher(uri);
        while (matcher.find()) {
            tokens.add(matcher.group(1));
        }
        return tokens;
    }
}
