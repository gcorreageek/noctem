package com.noctem.web.rest;

import com.noctem.NoctemApp;

import com.noctem.domain.Notification;
import com.noctem.repository.NotificationRepository;
import com.noctem.service.NotificationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotificationResource REST controller.
 *
 * @see NotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoctemApp.class)
public class NotificationResourceIntTest {


    private static final Boolean DEFAULT_SEND = false;
    private static final Boolean UPDATED_SEND = true;

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationService notificationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restNotificationMockMvc;

    private Notification notification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationResource notificationResource = new NotificationResource();
        ReflectionTestUtils.setField(notificationResource, "notificationService", notificationService);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification();
        notification.setSend(DEFAULT_SEND);
        return notification;
    }

    @Before
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notification)))
                .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notifications.get(notifications.size() - 1);
        assertThat(testNotification.isSend()).isEqualTo(DEFAULT_SEND);
    }

    @Test
    @Transactional
    public void checkSendIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setSend(null);

        // Create the Notification, which fails.

        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notification)))
                .andExpect(status().isBadRequest());

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notifications
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
                .andExpect(jsonPath("$.[*].send").value(hasItem(DEFAULT_SEND.booleanValue())));
    }

    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.send").value(DEFAULT_SEND.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findOne(notification.getId());
        updatedNotification.setSend(UPDATED_SEND);

        restNotificationMockMvc.perform(put("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotification)))
                .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notifications.get(notifications.size() - 1);
        assertThat(testNotification.isSend()).isEqualTo(UPDATED_SEND);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Get the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
