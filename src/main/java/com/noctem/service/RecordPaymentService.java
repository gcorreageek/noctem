package com.noctem.service;

import com.noctem.domain.RecordPayment;
import com.noctem.repository.RecordPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing RecordPayment.
 */
@Service
@Transactional
public class RecordPaymentService {

    private final Logger log = LoggerFactory.getLogger(RecordPaymentService.class);
    
    @Inject
    private RecordPaymentRepository recordPaymentRepository;

    /**
     * Save a recordPayment.
     *
     * @param recordPayment the entity to save
     * @return the persisted entity
     */
    public RecordPayment save(RecordPayment recordPayment) {
        log.debug("Request to save RecordPayment : {}", recordPayment);
        RecordPayment result = recordPaymentRepository.save(recordPayment);
        return result;
    }

    /**
     *  Get all the recordPayments.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RecordPayment> findAll() {
        log.debug("Request to get all RecordPayments");
        List<RecordPayment> result = recordPaymentRepository.findAll();

        return result;
    }

    /**
     *  Get one recordPayment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RecordPayment findOne(Long id) {
        log.debug("Request to get RecordPayment : {}", id);
        RecordPayment recordPayment = recordPaymentRepository.findOne(id);
        return recordPayment;
    }

    /**
     *  Delete the  recordPayment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RecordPayment : {}", id);
        recordPaymentRepository.delete(id);
    }
}
