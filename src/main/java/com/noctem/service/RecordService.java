package com.noctem.service;

import com.noctem.domain.Record;
import com.noctem.domain.RecordItem;
import com.noctem.repository.RecordItemRepository;
import com.noctem.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Record.
 */
@Service
@Transactional
public class RecordService {

    private final Logger log = LoggerFactory.getLogger(RecordService.class);

    @Inject
    private RecordRepository recordRepository;

    /**
     * Save a record.
     *
     * @param record the entity to save
     * @return the persisted entity
     */
    public Record save(Record record) {
        log.debug("Request to save Record : {}", record);
        for (RecordItem ri : record.getRecordItems()) {
            ri.setRecord(record);
        }
        Record result = recordRepository.save(record);
        return result;
    }

    /**
     *  Get all the records.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Record> findByUserIsCurrentUser() {
        log.debug("Request to get all Records");
        List<Record> result = recordRepository.findByUserIsCurrentUser();

        return result;
    }

    /**
     *  Get one record by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Record findOne(Long id) {
        log.debug("Request to get Record : {}", id);
        Record record = recordRepository.findOne(id);
        return record;
    }

    /**
     *  Delete the  record by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Record : {}", id);
        recordRepository.delete(id);
    }
}
