package com.noctem.service;

import com.noctem.domain.Card;
import com.noctem.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Card.
 */
@Service
@Transactional
public class CardService {

    private final Logger log = LoggerFactory.getLogger(CardService.class);

    @Inject
    private CardRepository cardRepository;

    /**
     * Save a card.
     *
     * @param card the entity to save
     * @return the persisted entity
     */
    public Card save(Card card) {
        log.debug("Request to save Card : {}", card);
        Card result = cardRepository.save(card);
        return result;
    }

    /**
     *  Get all the cards.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Card> findByUserIsCurrentUser() {
        log.debug("Request to get all ByUserIsCurrentUser");
        List<Card> result = cardRepository.findByUserIsCurrentUser();

        return result;
    }

    /**
     *  Get one card by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Card findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        Card card = cardRepository.findOne(id);
        return card;
    }

    /**
     *  Delete the  card by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.delete(id);
    }
}
