package com.noctem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noctem.domain.RecordItem;
import com.noctem.service.RecordItemService;
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
 * REST controller for managing RecordItem.
 */
@RestController
@RequestMapping("/api")
public class RecordItemResource {

    private final Logger log = LoggerFactory.getLogger(RecordItemResource.class);
        
    @Inject
    private RecordItemService recordItemService;

    /**
     * POST  /record-items : Create a new recordItem.
     *
     * @param recordItem the recordItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recordItem, or with status 400 (Bad Request) if the recordItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/record-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecordItem> createRecordItem(@Valid @RequestBody RecordItem recordItem) throws URISyntaxException {
        log.debug("REST request to save RecordItem : {}", recordItem);
        if (recordItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recordItem", "idexists", "A new recordItem cannot already have an ID")).body(null);
        }
        RecordItem result = recordItemService.save(recordItem);
        return ResponseEntity.created(new URI("/api/record-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recordItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /record-items : Updates an existing recordItem.
     *
     * @param recordItem the recordItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recordItem,
     * or with status 400 (Bad Request) if the recordItem is not valid,
     * or with status 500 (Internal Server Error) if the recordItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/record-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecordItem> updateRecordItem(@Valid @RequestBody RecordItem recordItem) throws URISyntaxException {
        log.debug("REST request to update RecordItem : {}", recordItem);
        if (recordItem.getId() == null) {
            return createRecordItem(recordItem);
        }
        RecordItem result = recordItemService.save(recordItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recordItem", recordItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /record-items : get all the recordItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recordItems in body
     */
    @RequestMapping(value = "/record-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RecordItem> getAllRecordItems() {
        log.debug("REST request to get all RecordItems");
        return recordItemService.findAll();
    }

    /**
     * GET  /record-items/:id : get the "id" recordItem.
     *
     * @param id the id of the recordItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recordItem, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/record-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecordItem> getRecordItem(@PathVariable Long id) {
        log.debug("REST request to get RecordItem : {}", id);
        RecordItem recordItem = recordItemService.findOne(id);
        return Optional.ofNullable(recordItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /record-items/:id : delete the "id" recordItem.
     *
     * @param id the id of the recordItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/record-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecordItem(@PathVariable Long id) {
        log.debug("REST request to delete RecordItem : {}", id);
        recordItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recordItem", id.toString())).build();
    }

}
