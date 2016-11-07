package com.noctem.web.rest;

import com.noctem.NoctemApp;

import com.noctem.domain.Card;
import com.noctem.repository.CardRepository;
import com.noctem.service.CardService;

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

import com.noctem.domain.enumeration.TypeCard;
/**
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoctemApp.class)
public class CardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final TypeCard DEFAULT_TYPE_CARD = TypeCard.VISA;
    private static final TypeCard UPDATED_TYPE_CARD = TypeCard.MASTERCARD;
    private static final String DEFAULT_NUMBER = "AAAAA";
    private static final String UPDATED_NUMBER = "BBBBB";

    @Inject
    private CardRepository cardRepository;

    @Inject
    private CardService cardService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCardMockMvc;

    private Card card;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardResource cardResource = new CardResource();
        ReflectionTestUtils.setField(cardResource, "cardService", cardService);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card();
        card.setName(DEFAULT_NAME);
        card.setTypeCard(DEFAULT_TYPE_CARD);
        card.setNumber(DEFAULT_NUMBER);
        return card;
    }

    @Before
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card)))
                .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cards.get(cards.size() - 1);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCard.getTypeCard()).isEqualTo(DEFAULT_TYPE_CARD);
        assertThat(testCard.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setName(null);

        // Create the Card, which fails.

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card)))
                .andExpect(status().isBadRequest());

        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeCardIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setTypeCard(null);

        // Create the Card, which fails.

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card)))
                .andExpect(status().isBadRequest());

        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setNumber(null);

        // Create the Card, which fails.

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(card)))
                .andExpect(status().isBadRequest());

        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cards
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].typeCard").value(hasItem(DEFAULT_TYPE_CARD.toString())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.typeCard").value(DEFAULT_TYPE_CARD.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardService.save(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findOne(card.getId());
        updatedCard.setName(UPDATED_NAME);
        updatedCard.setTypeCard(UPDATED_TYPE_CARD);
        updatedCard.setNumber(UPDATED_NUMBER);

        restCardMockMvc.perform(put("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCard)))
                .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cards.get(cards.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getTypeCard()).isEqualTo(UPDATED_TYPE_CARD);
        assertThat(testCard.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardService.save(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeDelete - 1);
    }
}
