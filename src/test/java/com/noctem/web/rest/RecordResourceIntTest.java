package com.noctem.web.rest;

import com.noctem.NoctemApp;

import com.noctem.domain.Record;
import com.noctem.repository.RecordRepository;
import com.noctem.service.RecordService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RecordResource REST controller.
 *
 * @see RecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoctemApp.class)
public class RecordResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    @Inject
    private RecordRepository recordRepository;

    @Inject
    private RecordService recordService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRecordMockMvc;

    private Record record;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecordResource recordResource = new RecordResource();
        ReflectionTestUtils.setField(recordResource, "recordService", recordService);
        this.restRecordMockMvc = MockMvcBuilders.standaloneSetup(recordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createEntity(EntityManager em) {
        Record record = new Record();
        record.setName(DEFAULT_NAME);
        record.setDate(DEFAULT_DATE);
        record.setTotal(DEFAULT_TOTAL);
        return record;
    }

    @Before
    public void initTest() {
        record = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecord() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        // Create the Record

        restRecordMockMvc.perform(post("/api/records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(record)))
                .andExpect(status().isCreated());

        // Validate the Record in the database
        List<Record> records = recordRepository.findAll();
        assertThat(records).hasSize(databaseSizeBeforeCreate + 1);
        Record testRecord = records.get(records.size() - 1);
        assertThat(testRecord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecord.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecord.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setName(null);

        // Create the Record, which fails.

        restRecordMockMvc.perform(post("/api/records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(record)))
                .andExpect(status().isBadRequest());

        List<Record> records = recordRepository.findAll();
        assertThat(records).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setDate(null);

        // Create the Record, which fails.

        restRecordMockMvc.perform(post("/api/records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(record)))
                .andExpect(status().isBadRequest());

        List<Record> records = recordRepository.findAll();
        assertThat(records).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setTotal(null);

        // Create the Record, which fails.

        restRecordMockMvc.perform(post("/api/records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(record)))
                .andExpect(status().isBadRequest());

        List<Record> records = recordRepository.findAll();
        assertThat(records).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecords() throws Exception {
        // Initialize the database
        recordRepository.saveAndFlush(record);

        // Get all the records
        restRecordMockMvc.perform(get("/api/records?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(record.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getRecord() throws Exception {
        // Initialize the database
        recordRepository.saveAndFlush(record);

        // Get the record
        restRecordMockMvc.perform(get("/api/records/{id}", record.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(record.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecord() throws Exception {
        // Get the record
        restRecordMockMvc.perform(get("/api/records/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecord() throws Exception {
        // Initialize the database
        recordService.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record
        Record updatedRecord = recordRepository.findOne(record.getId());
        updatedRecord.setName(UPDATED_NAME);
        updatedRecord.setDate(UPDATED_DATE);
        updatedRecord.setTotal(UPDATED_TOTAL);

        restRecordMockMvc.perform(put("/api/records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecord)))
                .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> records = recordRepository.findAll();
        assertThat(records).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = records.get(records.size() - 1);
        assertThat(testRecord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecord.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void deleteRecord() throws Exception {
        // Initialize the database
        recordService.save(record);

        int databaseSizeBeforeDelete = recordRepository.findAll().size();

        // Get the record
        restRecordMockMvc.perform(delete("/api/records/{id}", record.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Record> records = recordRepository.findAll();
        assertThat(records).hasSize(databaseSizeBeforeDelete - 1);
    }
}
