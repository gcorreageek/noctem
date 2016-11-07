package com.noctem.web.rest;

import com.noctem.NoctemApp;

import com.noctem.domain.RecordPayment;
import com.noctem.repository.RecordPaymentRepository;
import com.noctem.service.RecordPaymentService;

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
 * Test class for the RecordPaymentResource REST controller.
 *
 * @see RecordPaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoctemApp.class)
public class RecordPaymentResourceIntTest {


    private static final Double DEFAULT_PAY = 1D;
    private static final Double UPDATED_PAY = 2D;

    private static final Boolean DEFAULT_ACCEPT_PAYMENT = false;
    private static final Boolean UPDATED_ACCEPT_PAYMENT = true;

    @Inject
    private RecordPaymentRepository recordPaymentRepository;

    @Inject
    private RecordPaymentService recordPaymentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRecordPaymentMockMvc;

    private RecordPayment recordPayment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecordPaymentResource recordPaymentResource = new RecordPaymentResource();
        ReflectionTestUtils.setField(recordPaymentResource, "recordPaymentService", recordPaymentService);
        this.restRecordPaymentMockMvc = MockMvcBuilders.standaloneSetup(recordPaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordPayment createEntity(EntityManager em) {
        RecordPayment recordPayment = new RecordPayment();
        recordPayment.setPay(DEFAULT_PAY);
        recordPayment.setAcceptPayment(DEFAULT_ACCEPT_PAYMENT);
        return recordPayment;
    }

    @Before
    public void initTest() {
        recordPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecordPayment() throws Exception {
        int databaseSizeBeforeCreate = recordPaymentRepository.findAll().size();

        // Create the RecordPayment

        restRecordPaymentMockMvc.perform(post("/api/record-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordPayment)))
                .andExpect(status().isCreated());

        // Validate the RecordPayment in the database
        List<RecordPayment> recordPayments = recordPaymentRepository.findAll();
        assertThat(recordPayments).hasSize(databaseSizeBeforeCreate + 1);
        RecordPayment testRecordPayment = recordPayments.get(recordPayments.size() - 1);
        assertThat(testRecordPayment.getPay()).isEqualTo(DEFAULT_PAY);
        assertThat(testRecordPayment.isAcceptPayment()).isEqualTo(DEFAULT_ACCEPT_PAYMENT);
    }

    @Test
    @Transactional
    public void checkPayIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordPaymentRepository.findAll().size();
        // set the field null
        recordPayment.setPay(null);

        // Create the RecordPayment, which fails.

        restRecordPaymentMockMvc.perform(post("/api/record-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordPayment)))
                .andExpect(status().isBadRequest());

        List<RecordPayment> recordPayments = recordPaymentRepository.findAll();
        assertThat(recordPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptPaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordPaymentRepository.findAll().size();
        // set the field null
        recordPayment.setAcceptPayment(null);

        // Create the RecordPayment, which fails.

        restRecordPaymentMockMvc.perform(post("/api/record-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordPayment)))
                .andExpect(status().isBadRequest());

        List<RecordPayment> recordPayments = recordPaymentRepository.findAll();
        assertThat(recordPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecordPayments() throws Exception {
        // Initialize the database
        recordPaymentRepository.saveAndFlush(recordPayment);

        // Get all the recordPayments
        restRecordPaymentMockMvc.perform(get("/api/record-payments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recordPayment.getId().intValue())))
                .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())))
                .andExpect(jsonPath("$.[*].acceptPayment").value(hasItem(DEFAULT_ACCEPT_PAYMENT.booleanValue())));
    }

    @Test
    @Transactional
    public void getRecordPayment() throws Exception {
        // Initialize the database
        recordPaymentRepository.saveAndFlush(recordPayment);

        // Get the recordPayment
        restRecordPaymentMockMvc.perform(get("/api/record-payments/{id}", recordPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recordPayment.getId().intValue()))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.doubleValue()))
            .andExpect(jsonPath("$.acceptPayment").value(DEFAULT_ACCEPT_PAYMENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecordPayment() throws Exception {
        // Get the recordPayment
        restRecordPaymentMockMvc.perform(get("/api/record-payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecordPayment() throws Exception {
        // Initialize the database
        recordPaymentService.save(recordPayment);

        int databaseSizeBeforeUpdate = recordPaymentRepository.findAll().size();

        // Update the recordPayment
        RecordPayment updatedRecordPayment = recordPaymentRepository.findOne(recordPayment.getId());
        updatedRecordPayment.setPay(UPDATED_PAY);
        updatedRecordPayment.setAcceptPayment(UPDATED_ACCEPT_PAYMENT);

        restRecordPaymentMockMvc.perform(put("/api/record-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecordPayment)))
                .andExpect(status().isOk());

        // Validate the RecordPayment in the database
        List<RecordPayment> recordPayments = recordPaymentRepository.findAll();
        assertThat(recordPayments).hasSize(databaseSizeBeforeUpdate);
        RecordPayment testRecordPayment = recordPayments.get(recordPayments.size() - 1);
        assertThat(testRecordPayment.getPay()).isEqualTo(UPDATED_PAY);
        assertThat(testRecordPayment.isAcceptPayment()).isEqualTo(UPDATED_ACCEPT_PAYMENT);
    }

    @Test
    @Transactional
    public void deleteRecordPayment() throws Exception {
        // Initialize the database
        recordPaymentService.save(recordPayment);

        int databaseSizeBeforeDelete = recordPaymentRepository.findAll().size();

        // Get the recordPayment
        restRecordPaymentMockMvc.perform(delete("/api/record-payments/{id}", recordPayment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RecordPayment> recordPayments = recordPaymentRepository.findAll();
        assertThat(recordPayments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
