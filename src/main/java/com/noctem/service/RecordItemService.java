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
    public RecordItem save(RecordItem recordItem) {
        log.debug("Request to save RecordItem : {}", recordItem);
        RecordItem result = recordItemRepository.save(recordItem);
        return result;
    }

    @Transactional(readOnly = true)
    public List<RecordItem> findAll() {
        log.debug("Request to get all RecordItems");
        List<RecordItem> result = recordItemRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public List<RecordItem> findByRecordId(Long idRecord) {
        log.debug("Request to get all findByRecordId");
        List<RecordItem> result = recordItemRepository.findByRecordId(idRecord);
        return result;
    }

    @Transactional(readOnly = true)
    public RecordItem findOne(Long id) {
        log.debug("Request to get RecordItem : {}", id);
        RecordItem recordItem = recordItemRepository.findOne(id);
        return recordItem;
    }

    public void delete(Long id) {
        log.debug("Request to delete RecordItem : {}", id);
        recordItemRepository.delete(id);
    }
}
