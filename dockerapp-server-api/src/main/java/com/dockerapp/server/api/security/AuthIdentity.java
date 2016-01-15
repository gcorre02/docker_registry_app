package com.dockerapp.server.api.security;

import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

public final class AuthIdentity {
    private final String identity;
    private final Role role;
    private final Set<Permission> permissions;

    public AuthIdentity(String identity, Role role) {
        this.identity = identity;
        this.role = Objects.requireNonNull(role);
        this.permissions = Objects.requireNonNull(RolePermissions.getPermissionsFor(role));
    }

    public String getIdentity() {
        return identity;
    }

    public Role getRole() {
        return role;
    }

    public boolean hasPermissions(Set<Permission> permissions) {
        return !Sets.intersection(permissions, this.permissions).isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", identity, role);
    }
}
