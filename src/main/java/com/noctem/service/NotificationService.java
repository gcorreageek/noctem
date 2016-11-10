package com.noctem.service;

import com.noctem.domain.Notification;
import com.noctem.domain.UserGroup;
import com.noctem.domain.enumeration.TypeEvent;
import com.noctem.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Service Implementation for managing Notification.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Inject
    private NotificationRepository notificationRepository;
    @Inject
    private ExcelService excelService;
    @Inject
    private MailService mailService;

    /**
     * Save a notification.
     *
     * @param notification the entity to save
     * @return the persisted entity
     */
    public Notification save(Notification notification) throws IOException {
        log.debug("Request to save Notification : {}"   , notification);
        Notification result = notificationRepository.save(notification);
        if(result.isSend()){
            Set<UserGroup> userGroupSet = result.getGroups().getUserGroups();
            Boolean withList = false;
            if(notification.getEvent().getTypeEvent().name().toString().equals(TypeEvent.LIST.name().toString())){
                withList = true;
            }
            final Boolean finalWithList = withList;
            final CountDownLatch latch = new CountDownLatch(1);
            new Thread("Thread excel") {
                public void run() {
                    createExcel(finalWithList, userGroupSet);
                    latch.countDown();
                }
            }.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("error await",e);
            }
            sendEmail(userGroupSet, notification, finalWithList);

        }
        return result;
    }

    private void createExcel(Boolean finalWithList, Set<UserGroup> userGroupSet) {
        if(finalWithList){
            try {
                excelService.writeExcel(userGroupSet);
            } catch (IOException e) {
                log.error("error excel",e);
            }
        }
    }

    private void sendEmail(Set<UserGroup> userGroupSet, Notification notification, Boolean finalWithList) {
        for (UserGroup userGroup : userGroupSet) {
            mailService.sendEmail(userGroup.getEmail(),notification.getEvent().getSubject(),notification.getEvent().getMessage(), finalWithList,true);
        }
    }

    /**
     *  Get all the notifications.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Notification> findByUserIsCurrentUser() {
        log.debug("Request to get all Notifications");
        List<Notification> result = notificationRepository.findByUserIsCurrentUser();

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
