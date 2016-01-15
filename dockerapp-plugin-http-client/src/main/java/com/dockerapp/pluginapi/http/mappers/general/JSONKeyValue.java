package com.dockerapp.pluginapi.http.mappers.general;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;

/**
 * Example of a json mapped class
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"field", "value"})
public class JSONKeyValue {

    @JsonProperty("field")
    private String field;

    @JsonProperty("value")
    private String value;

    @JsonProperty("field")
    public void setField(String field) {
        this.field = field;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("field")
    public String getField() {
        return field;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }


}
