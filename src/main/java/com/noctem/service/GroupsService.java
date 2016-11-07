package com.noctem.service;

import com.noctem.domain.Authority;
import com.noctem.domain.Groups;
import com.noctem.domain.User;
import com.noctem.domain.UserGroup;
import com.noctem.repository.GroupsRepository;
import com.noctem.repository.UserGroupRepository;
import com.noctem.security.AuthoritiesConstants;
import com.noctem.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Groups.
 */
@Service
public class GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsService.class);

    @Inject
    private GroupsRepository groupsRepository;
    @Inject
    private UserService userService;
    @Inject
    private UserGroupService userGroupService;
    @Inject
    private UserGroupRepository userGroupRepository;

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        /*Groups result = null;
        Set<UserGroup> userGroupSet = new HashSet<UserGroup>();
        for (UserGroup userGroup : groups.getUserGroups()) {
            UserGroup userGroup1 = new UserGroup();
            userGroup1.setId(userGroup.getId());
            userGroup1.setName(userGroup.getName());
            userGroup1.setEmail(userGroup.getEmail());
            userGroup1.setGroups(groups);
            userGroupSet.add(userGroup1);
        }
        groups.setUserGroups(userGroupSet);
        result = groupsRepository.save(groups);
        return result;*/
        for (UserGroup userGroup : groups.getUserGroups()) {
            userGroup.setGroups(groups);
        } 
        return groupsRepository.save(groups);
    }

    /**
     *  Get all the groups.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Groups> findAll() {
        log.debug("Request to get all Groups");
        List<Groups> result = groupsRepository.findAll();
        return result;
    }
    @Transactional(readOnly = true)
    public List<Groups> findByAuthority() {
        /*Boolean isAdmin = false;
        Set<Authority> authoritySet =  userService.getUserWithAuthorities().getAuthorities();
        for (Authority authority : authoritySet) {
            log.debug("getName():"+authority.getName());
            if(authority.getName().equals(AuthoritiesConstants.ADMIN)){
                isAdmin = true;
                break;
            }
        }
        if(isAdmin){
            return groupsRepository.findAll();
        }else{
            List<Groups> groups = groupsRepository.findByUserIsCurrentUser();
            for (Groups group : groups) {
                log.debug("group:"+group.getName());
            }
            return groups;
        }*/
        List<Groups> groups = groupsRepository.findByUserIsCurrentUser();
        for (Groups group : groups) {
            log.debug("group:"+group.getName());
        }
        return groups;
    }

    /**
     *  Get one groups by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Groups findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        Groups groups = groupsRepository.findOne(id);
        return groups;
    }

    /**
     *  Delete the  groups by id.
     *
     *  @param id the id of the entity
     */

    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
    }
}
