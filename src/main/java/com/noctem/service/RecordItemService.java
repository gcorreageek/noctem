package com.noctem.service;

import com.noctem.domain.RecordItem;
import com.noctem.repository.RecordItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing RecordItem.
 */
@Service
@Transactional
public class RecordItemService {

    private final Logger log = LoggerFactory.getLogger(RecordItemService.class);
    
    @Inject
    private RecordItemRepository recordItemRepository;

    /**
     * Save a recordItem.
     *
     * @param recordItem the entity to save
     * @return the persisted entity
     */
    public RecordItem save(RecordItem recordItem) {
        log.debug("Request to save RecordItem : {}", recordItem);
        RecordItem result = recordItemRepository.save(recordItem);
        return result;
    }

    /**
     *  Get all the recordItems.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RecordItem> findAll() {
        log.debug("Request to get all RecordItems");
        List<RecordItem> result = recordItemRepository.findAll();

        return result;
    }

    /**
     *  Get one recordItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RecordItem findOne(Long id) {
        log.debug("Request to get RecordItem : {}", id);
        RecordItem recordItem = recordItemRepository.findOne(id);
        return recordItem;
    }

    /**
     *  Delete the  recordItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RecordItem : {}", id);
        recordItemRepository.delete(id);
    }
}
