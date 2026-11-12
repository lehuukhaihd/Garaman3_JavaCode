package com.garaman3.entity;

import java.io.Serializable;

public final class StaffAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String username;
    private final String fullName;
    private final String role;

    public StaffAccount(String id, String username, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public boolean canViewSupplierReports() {
        return role != null && "Manager".equalsIgnoreCase(role.trim());
    }
}
