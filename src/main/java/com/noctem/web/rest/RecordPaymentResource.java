package com.noctem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noctem.domain.RecordPayment;
import com.noctem.service.RecordPaymentService;
import com.noctem.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RecordPayment.
 */
@RestController
@RequestMapping("/api")
public class RecordPaymentResource {

    private final Logger log = LoggerFactory.getLogger(RecordPaymentResource.class);

    @Inject
    private RecordPaymentService recordPaymentService;

    /**
     * POST  /record-payments : Create a new recordPayment.
     *
     * @param recordPayment the recordPayment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recordPayment, or with status 400 (Bad Request) if the recordPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/record-payments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecordPayment> createRecordPayment(@Valid @RequestBody RecordPayment recordPayment) throws URISyntaxException {
        log.debug("REST request to save RecordPayment : {}", recordPayment);
        if (recordPayment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recordPayment", "idexists", "A new recordPayment cannot already have an ID")).body(null);
        }
        RecordPayment result = recordPaymentService.save(recordPayment);
        return ResponseEntity.created(new URI("/api/record-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recordPayment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /record-payments : Updates an existing recordPayment.
     *
     * @param recordPayment the recordPayment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recordPayment,
     * or with status 400 (Bad Request) if the recordPayment is not valid,
     * or with status 500 (Internal Server Error) if the recordPayment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/record-payments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecordPayment> updateRecordPayment(@Valid @RequestBody RecordPayment recordPayment) throws URISyntaxException {
        log.debug("REST request to update RecordPayment : {}", recordPayment);
        if (recordPayment.getId() == null) {
            return createRecordPayment(recordPayment);
        }
        RecordPayment result = recordPaymentService.save(recordPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recordPayment", recordPayment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /record-payments : get all the recordPayments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recordPayments in body
     */
    @RequestMapping(value = "/record-payments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RecordPayment> getAllRecordPaymentsByAuthority() {
        log.debug("REST request to get all RecordPayments");
        return recordPaymentService.findAllByAuthority();
    }

    /**
     * GET  /record-payments/:id : get the "id" recordPayment.
     *
     * @param id the id of the recordPayment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recordPayment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/record-payments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecordPayment> getRecordPayment(@PathVariable Long id) {
        log.debug("REST request to get RecordPayment : {}", id);
        RecordPayment recordPayment = recordPaymentService.findOne(id);
        return Optional.ofNullable(recordPayment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /record-payments/:id : delete the "id" recordPayment.
     *
     * @param id the id of the recordPayment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/record-payments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecordPayment(@PathVariable Long id) {
        log.debug("REST request to delete RecordPayment : {}", id);
        recordPaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recordPayment", id.toString())).build();
    }

}
