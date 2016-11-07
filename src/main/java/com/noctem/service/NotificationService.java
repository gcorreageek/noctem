package com.noctem.service;

import com.noctem.domain.Notification;
import com.noctem.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Notification.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);
    
    @Inject
    private NotificationRepository notificationRepository;

    /**
     * Save a notification.
     *
     * @param notification the entity to save
     * @return the persisted entity
     */
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        Notification result = notificationRepository.save(notification);
        return result;
    }

    /**
     *  Get all the notifications.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Notification> findAll() {
        log.debug("Request to get all Notifications");
        List<Notification> result = notificationRepository.findAll();

        return result;
    }

    /**
     *  Get one notification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Notification findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return notification;
    }

    /**
     *  Delete the  notification by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.delete(id);
    }
}
