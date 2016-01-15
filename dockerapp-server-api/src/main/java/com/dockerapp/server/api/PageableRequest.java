package com.dockerapp.server.api;

import com.dockerapp.dao.error.Check;
import com.dockerapp.dao.error.DockerAppError;
import com.google.common.collect.Sets;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class PageableRequest {
    private Integer page;
    private Integer pageSize;

    private List<String> sortOrder;
    private Boolean sortDescending;

    public Pageable getPageable() {
        return new PageRequest(
                getPage(),
                getPageSize(),
                getSortDescending() ? Sort.Direction.DESC : Sort.Direction.ASC,
                getSortOrder());
    }

    public Integer getPage() {
        Check.validate(page == null || page >= 0, DockerAppError.VALIDATION_FAILED, "page can't be negative");
        return page == null ? 0 : page;
    }

    public Integer getPageSize() {
        Check.validate(pageSize == null || (pageSize > 0 && pageSize <= 100),
                DockerAppError.VALIDATION_FAILED,
                "page size must be between 1 and 100");
        return pageSize == null ? getDefaultPageSize() : pageSize;
    }

    public String[] getSortOrder() {
        List<String> sortOrder = this.sortOrder == null ? getDefaultSortColumns() : this.sortOrder;
        Check.validate(getAllowedSortColumns().containsAll(sortOrder), DockerAppError.VALIDATION_FAILED,
                String.format("invalid sort order column(s) %s, valid values are %s",
                        Sets.difference(new LinkedHashSet<>(sortOrder), getAllowedSortColumns()),
                        getAllowedSortColumns()));
        return sortOrder.toArray(new String[sortOrder.size()]);
    }

    public Boolean getSortDescending() {
        return sortDescending == null ? isDefaultSortDescending() : sortDescending;
    }

    protected int getDefaultPageSize() {
        return 10;
    }

    protected boolean isDefaultSortDescending() {
        return true;
    }

    protected abstract Set<String> getAllowedSortColumns();

    protected abstract List<String> getDefaultSortColumns();
}
