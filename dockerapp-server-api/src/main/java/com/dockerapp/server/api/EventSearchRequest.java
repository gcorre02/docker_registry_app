package com.dockerapp.server.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

public class EventSearchRequest extends PageableRequest {

    private String stubId;
    private String userId;
    private String emailAddress;
    private boolean excludeCustomerEvents;

    public String getStubId() {
        return stubId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public boolean isExcludeCustomerEvents() {
        return excludeCustomerEvents;
    }

    @Override
    protected boolean isDefaultSortDescending() {
        return true;
    }

    @Override
    protected Set<String> getAllowedSortColumns() {
        return ImmutableSet.of("eventTs", "id");
    }

    @Override
    protected List<String> getDefaultSortColumns() {
        return ImmutableList.of("eventTs", "id");
    }
}
