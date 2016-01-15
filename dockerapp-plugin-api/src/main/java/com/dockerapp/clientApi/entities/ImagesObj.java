package com.dockerapp.clientApi.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.List;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder("name, tags")
@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ImagesObj implements JsonObject {
    @JsonProperty("name")
    private String name;
    @JsonProperty("tags")
    private List<String> tags;

    public ImagesObj(Map<String, Object> imageMaps) {
        this.name = (String) imageMaps.get("name");
        this.tags = (List<String>) imageMaps.get("tags");
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("name")
    public void setName(String name) {

        this.name = name;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    public ImagesObj(String name, List<String> tags) {
        this.name = name;
        this.tags = tags;
    }
}