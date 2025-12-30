package com.robertgarcia.template.modules.notifications.service;


import com.robertgarcia.template.modules.notifications.domain.Notification;
import com.robertgarcia.template.modules.notifications.repo.NotificationRepository;
import com.robertgarcia.template.shared.crud.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class NotificationService implements CrudService<Notification,Integer> {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public List<Notification> findAll() {
        return notificationRepository.findAllByDeletedIsFalse();
    }


    public Notification findById(Integer id) {
        return notificationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public Notification save(Notification entity) {
        entity.setDeleted(false);
        return notificationRepository.save(entity);
    }


    public void delete(Notification entity) {
        entity.setDeleted(true);
        notificationRepository.save(entity);
    }

    public List<Notification> findByClientId(Integer id){
        return notificationRepository.findAllByDeletedIsFalse();
    }
}

