package com.tre3p.fileserver.model;

public enum Role {
    USER("user");
//    ADMIN("admin");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
