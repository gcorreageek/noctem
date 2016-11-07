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
import org.springframework.security.access.prepost.PreAuthorize;
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


//    @PreAuthorize("checkPermission('Purchaserequest','Manager','Create')")
//    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<?> save(
//        @RequestBody  PurchaseRequest  purchaseRequest

    @RequestMapping(value = "/groups", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Groups> createGroups( @RequestBody Groups groups) throws URISyntaxException {
        log.debug("REST request to save Groups : {}", groups);
        if(groups==null){
            log.debug("es null" );
        }else{
            log.debug("size:"+groups.getUserGroups().size());
        }

        if (groups.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("groups", "idexists", "A new groups cannot already have an ID")).body(null);
        }
        Groups result = groupsService.save(groups);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groups", result.getId().toString()))
            .body(result);
    }


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


    @RequestMapping(value = "/groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Groups> getAllGroups() {
        log.debug("REST request to get all Groups");
        return groupsService.findByAuthority();
    }


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
