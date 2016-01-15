package com.dockerapp.server.api.security;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RolePermissions {
    private static final Map<Role, RolePermissions> ROLE_PERMISSIONS = buildPermissions();

    private final Role role;
    private final Set<Permission> permissions;

    private RolePermissions(Role role, Set<Permission> permissions) {
        this.role = role;
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public static RolePermissions getRolePermissionsFor(Role role) {
        return ROLE_PERMISSIONS.get(role);
    }

    public static Set<Permission> getPermissionsFor(Role role) {
        return ROLE_PERMISSIONS.get(role).getPermissions();
    }

    public Role getRole() {
        return role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    private static Map<Role, RolePermissions> buildPermissions() {
        List<RolePermissions> list = ImmutableList.of(
                new RolePermissions(Role.UNAUTHENTICATED, EnumSet.of(
                        Permission.TEST)),
                new RolePermissions(Role.AUTOMATION, EnumSet.noneOf(Permission.class)),
                new RolePermissions(Role.USER, EnumSet.of(
                        Permission.STUB_PERMISSION_1,
                        Permission.STUB_PERMISSION_2)),
                new RolePermissions(Role.DOCKERAPP_ADMIN, EnumSet.of(
                        Permission.STUB_PERMISSION_1,
                        Permission.STUB_PERMISSION_2,
                        Permission.EVENT_LOG_SEARCH)));

        ImmutableMap.Builder<Role, RolePermissions> builder = ImmutableMap.builder();
        for (RolePermissions rolePermissions : list) {
            builder.put(rolePermissions.getRole(), rolePermissions);
        }
        return  builder.build();
    }
}
