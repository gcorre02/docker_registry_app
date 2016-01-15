package com.dockerapp.server.dto;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PageDto<T> {
    private final int page;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final List<T> results;

    public PageDto(int page, int pageSize, int totalPages, long totalElements, List<T> results) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.results = results;
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                '}';
    }
}
