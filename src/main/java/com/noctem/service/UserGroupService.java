package com.noctem.service;

import com.noctem.domain.UserGroup;
import com.noctem.repository.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing UserGroup.
 */
@Service
@Transactional
public class UserGroupService {

    private final Logger log = LoggerFactory.getLogger(UserGroupService.class);

    @Inject
    private UserGroupRepository userGroupRepository;

    /**
     * Save a userGroup.
     *
     * @param userGroup the entity to save
     * @return the persisted entity
     */
    public UserGroup save(UserGroup userGroup) {
        log.debug("Request to save UserGroup : {}", userGroup);
        UserGroup result = userGroupRepository.save(userGroup);
        return result;
    }

    /**
     *  Get all the userGroups.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<UserGroup> findAll() {
        log.debug("Request to get all UserGroups");
        List<UserGroup> result = userGroupRepository.findAll();

        return result;
    }
    @Transactional(readOnly = true)
    public List<UserGroup> findByGroupsId(Long idGroups) {
        return userGroupRepository.findByGroupsId(idGroups);
    }

    /**
     *  Get one userGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public UserGroup findOne(Long id) {
        log.debug("Request to get UserGroup : {}", id);
        UserGroup userGroup = userGroupRepository.findOne(id);
        return userGroup;
    }

    /**
     *  Delete the  userGroup by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserGroup : {}", id);
        userGroupRepository.delete(id);
    }
}
