package com.noctem.web.rest;

import com.noctem.NoctemApp;

import com.noctem.domain.Groups;
import com.noctem.repository.GroupsRepository;
import com.noctem.service.GroupsService;

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
 * Test class for the GroupsResource REST controller.
 *
 * @see GroupsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoctemApp.class)
public class GroupsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private GroupsRepository groupsRepository;

    @Inject
    private GroupsService groupsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGroupsMockMvc;

    private Groups groups;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupsResource groupsResource = new GroupsResource();
        ReflectionTestUtils.setField(groupsResource, "groupsService", groupsService);
        this.restGroupsMockMvc = MockMvcBuilders.standaloneSetup(groupsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Groups createEntity(EntityManager em) {
        Groups groups = new Groups();
        groups.setName(DEFAULT_NAME);
        return groups;
    }

    @Before
    public void initTest() {
        groups = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroups() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups

        restGroupsMockMvc.perform(post("/api/groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groups)))
                .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groups = groupsRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeCreate + 1);
        Groups testGroups = groups.get(groups.size() - 1);
        assertThat(testGroups.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsRepository.findAll().size();
        // set the field null
        groups.setName(null);

        // Create the Groups, which fails.

        restGroupsMockMvc.perform(post("/api/groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groups)))
                .andExpect(status().isBadRequest());

        List<Groups> groups = groupsRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groups
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", groups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groups.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroups() throws Exception {
        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups
        Groups updatedGroups = groupsRepository.findOne(groups.getId());
        updatedGroups.setName(UPDATED_NAME);

        restGroupsMockMvc.perform(put("/api/groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGroups)))
                .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groups = groupsRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groups.get(groups.size() - 1);
        assertThat(testGroups.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        int databaseSizeBeforeDelete = groupsRepository.findAll().size();

        // Get the groups
        restGroupsMockMvc.perform(delete("/api/groups/{id}", groups.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Groups> groups = groupsRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
