package com.robertgarcia.template.modules.notifications.repo;

import com.robertgarcia.template.modules.notifications.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByDeletedIsFalse();
}