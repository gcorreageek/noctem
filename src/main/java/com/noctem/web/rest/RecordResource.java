package com.noctem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noctem.domain.Record;
import com.noctem.service.RecordService;
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
 * REST controller for managing Record.
 */
@RestController
@RequestMapping("/api")
public class RecordResource {

    private final Logger log = LoggerFactory.getLogger(RecordResource.class);

    @Inject
    private RecordService recordService;

    /**
     * POST  /records : Create a new record.
     *
     * @param record the record to create
     * @return the ResponseEntity with status 201 (Created) and with body the new record, or with status 400 (Bad Request) if the record has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/records",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Record> createRecord(@Valid @RequestBody Record record) throws URISyntaxException {
        log.debug("REST request to save Record : {}", record);
        if (record.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("record", "idexists", "A new record cannot already have an ID")).body(null);
        }
        Record result = recordService.save(record);
        return ResponseEntity.created(new URI("/api/records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("record", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /records : Updates an existing record.
     *
     * @param record the record to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated record,
     * or with status 400 (Bad Request) if the record is not valid,
     * or with status 500 (Internal Server Error) if the record couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/records",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Record> updateRecord(@Valid @RequestBody Record record) throws URISyntaxException {
        log.debug("REST request to update Record : {}", record);
        if (record.getId() == null) {
            return createRecord(record);
        }
        Record result = recordService.save(record);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("record", record.getId().toString()))
            .body(result);
    }

    /**
     * GET  /records : get all the records.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of records in body
     */
    @RequestMapping(value = "/records",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Record> getAllRecordsByUserIsCurrentUser() {
        log.debug("REST request to get all Records");
        return recordService.findByUserIsCurrentUser();
    }

    /**
     * GET  /records/:id : get the "id" record.
     *
     * @param id the id of the record to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the record, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/records/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Record> getRecord(@PathVariable Long id) {
        log.debug("REST request to get Record : {}", id);
        Record record = recordService.findOne(id);
        return Optional.ofNullable(record)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /records/:id : delete the "id" record.
     *
     * @param id the id of the record to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/records/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        log.debug("REST request to delete Record : {}", id);
        recordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("record", id.toString())).build();
    }

}
