package com.noctem.web.rest;

import com.noctem.NoctemApp;

import com.noctem.domain.RecordItem;
import com.noctem.repository.RecordItemRepository;
import com.noctem.service.RecordItemService;

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
 * Test class for the RecordItemResource REST controller.
 *
 * @see RecordItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoctemApp.class)
public class RecordItemResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    @Inject
    private RecordItemRepository recordItemRepository;

    @Inject
    private RecordItemService recordItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRecordItemMockMvc;

    private RecordItem recordItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecordItemResource recordItemResource = new RecordItemResource();
        ReflectionTestUtils.setField(recordItemResource, "recordItemService", recordItemService);
        this.restRecordItemMockMvc = MockMvcBuilders.standaloneSetup(recordItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordItem createEntity(EntityManager em) {
        RecordItem recordItem = new RecordItem();
        recordItem.setDescription(DEFAULT_DESCRIPTION);
        recordItem.setQuantity(DEFAULT_QUANTITY);
        recordItem.setPrice(DEFAULT_PRICE);
        recordItem.setTotal(DEFAULT_TOTAL);
        return recordItem;
    }

    @Before
    public void initTest() {
        recordItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecordItem() throws Exception {
        int databaseSizeBeforeCreate = recordItemRepository.findAll().size();

        // Create the RecordItem

        restRecordItemMockMvc.perform(post("/api/record-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordItem)))
                .andExpect(status().isCreated());

        // Validate the RecordItem in the database
        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeCreate + 1);
        RecordItem testRecordItem = recordItems.get(recordItems.size() - 1);
        assertThat(testRecordItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRecordItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testRecordItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRecordItem.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordItemRepository.findAll().size();
        // set the field null
        recordItem.setDescription(null);

        // Create the RecordItem, which fails.

        restRecordItemMockMvc.perform(post("/api/record-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordItem)))
                .andExpect(status().isBadRequest());

        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordItemRepository.findAll().size();
        // set the field null
        recordItem.setQuantity(null);

        // Create the RecordItem, which fails.

        restRecordItemMockMvc.perform(post("/api/record-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordItem)))
                .andExpect(status().isBadRequest());

        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordItemRepository.findAll().size();
        // set the field null
        recordItem.setPrice(null);

        // Create the RecordItem, which fails.

        restRecordItemMockMvc.perform(post("/api/record-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordItem)))
                .andExpect(status().isBadRequest());

        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordItemRepository.findAll().size();
        // set the field null
        recordItem.setTotal(null);

        // Create the RecordItem, which fails.

        restRecordItemMockMvc.perform(post("/api/record-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordItem)))
                .andExpect(status().isBadRequest());

        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecordItems() throws Exception {
        // Initialize the database
        recordItemRepository.saveAndFlush(recordItem);

        // Get all the recordItems
        restRecordItemMockMvc.perform(get("/api/record-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recordItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getRecordItem() throws Exception {
        // Initialize the database
        recordItemRepository.saveAndFlush(recordItem);

        // Get the recordItem
        restRecordItemMockMvc.perform(get("/api/record-items/{id}", recordItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recordItem.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecordItem() throws Exception {
        // Get the recordItem
        restRecordItemMockMvc.perform(get("/api/record-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecordItem() throws Exception {
        // Initialize the database
        recordItemService.save(recordItem);

        int databaseSizeBeforeUpdate = recordItemRepository.findAll().size();

        // Update the recordItem
        RecordItem updatedRecordItem = recordItemRepository.findOne(recordItem.getId());
        updatedRecordItem.setDescription(UPDATED_DESCRIPTION);
        updatedRecordItem.setQuantity(UPDATED_QUANTITY);
        updatedRecordItem.setPrice(UPDATED_PRICE);
        updatedRecordItem.setTotal(UPDATED_TOTAL);

        restRecordItemMockMvc.perform(put("/api/record-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecordItem)))
                .andExpect(status().isOk());

        // Validate the RecordItem in the database
        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeUpdate);
        RecordItem testRecordItem = recordItems.get(recordItems.size() - 1);
        assertThat(testRecordItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRecordItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRecordItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRecordItem.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void deleteRecordItem() throws Exception {
        // Initialize the database
        recordItemService.save(recordItem);

        int databaseSizeBeforeDelete = recordItemRepository.findAll().size();

        // Get the recordItem
        restRecordItemMockMvc.perform(delete("/api/record-items/{id}", recordItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RecordItem> recordItems = recordItemRepository.findAll();
        assertThat(recordItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
