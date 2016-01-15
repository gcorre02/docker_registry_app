package com.dockerapp.clientApi.entities;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.List;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder("repositories")
public class DockerRegistryRepositories implements JsonObject {
    @JsonProperty("repositories")
    private List<String> repositories;
    public DockerRegistryRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public DockerRegistryRepositories(Map<String, List<String>> repos) {
        this.repositories = repos.get("repositories");
    }

    @JsonProperty("repositories")
    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }
    @JsonProperty("repositories")
    public List<String> getRepositories() {
        return repositories;
    }

}