package com.noctem.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noctem.domain.Groups;
import com.noctem.service.GroupsService;
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
 * REST controller for managing Groups.
 */
@RestController
@RequestMapping("/api")
public class GroupsResource {

    private final Logger log = LoggerFactory.getLogger(GroupsResource.class);

    @Inject
    private GroupsService groupsService;

    /**
     * POST  /groups : Create a new groups.
     *
     * @param groups the groups to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groups, or with status 400 (Bad Request) if the groups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Groups> createGroups(@Valid @RequestBody Groups groups) throws URISyntaxException {
        log.debug("REST request to save Groups : {}", groups);
        if (groups.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("groups", "idexists", "A new groups cannot already have an ID")).body(null);
        }
        Groups result = groupsService.save(groups);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groups", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing groups.
     *
     * @param groups the groups to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groups,
     * or with status 400 (Bad Request) if the groups is not valid,
     * or with status 500 (Internal Server Error) if the groups couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/groups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Groups> updateGroups(@Valid @RequestBody Groups groups) throws URISyntaxException {
        log.debug("REST request to update Groups : {}", groups);
        if (groups.getId() == null) {
            return createGroups(groups);
        }
        Groups result = groupsService.save(groups);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("groups", groups.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @RequestMapping(value = "/groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Groups> getAllGroups() {
        log.debug("REST request to get all Groups");
        return groupsService.findByAuthority();
    }

    /**
     * GET  /groups/:id : get the "id" groups.
     *
     * @param id the id of the groups to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groups, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/groups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Groups> getGroups(@PathVariable Long id) {
        log.debug("REST request to get Groups : {}", id);
        Groups groups = groupsService.findOne(id);
        return Optional.ofNullable(groups)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /groups/:id : delete the "id" groups.
     *
     * @param id the id of the groups to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/groups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGroups(@PathVariable Long id) {
        log.debug("REST request to delete Groups : {}", id);
        groupsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("groups", id.toString())).build();
    }

}
