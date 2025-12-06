package com.robertgarcia.template.modules.users.domain;

import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission extends BasicEntity {
    private String permission;
    private String assignedPage;
    private String description;

    public Permission(String permission, String description, String assignedPage) {
        this.permission = permission;
        this.description = description;
        this.assignedPage = assignedPage;
    }

    public Permission() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAssignedPage() {
        return assignedPage;
    }

    public void setAssignedPage(String assignedPage) {
        this.assignedPage = assignedPage;
    }
}
