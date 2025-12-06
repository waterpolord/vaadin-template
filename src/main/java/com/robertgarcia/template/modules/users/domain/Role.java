package com.robertgarcia.template.modules.users.domain;

import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "role_app")
public class Role extends BasicEntity {
    @Column(length = 30, nullable = false, unique = true)
    private String name;
    @Column()
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Role() {

    }
}
