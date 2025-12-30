package com.robertgarcia.template.modules.notifications.domain;

import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Notification extends BasicEntity {

    private String title;
    private String description;
}

