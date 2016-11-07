package com.noctem.service;

import com.noctem.domain.Authority;
import com.noctem.domain.Groups;
import com.noctem.domain.User;
import com.noctem.domain.UserGroup;
import com.noctem.repository.GroupsRepository;
import com.noctem.security.AuthoritiesConstants;
import com.noctem.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Groups.
 */
@Service
@Transactional
public class GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsService.class);

    @Inject
    private GroupsRepository groupsRepository;
    @Inject
    private UserService userService;
    @Inject
    private UserGroupService userGroupService;

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        Groups result = null;
        try {
            Groups group  = new Groups();
            group.setId(groups.getId());
            group.setName(groups.getName());
            group.setUser(groups.getUser());
            result = groupsRepository.save(group);
            Set<UserGroup> userGroupSet = groups.getUserGroups();
            for (UserGroup userGroup : userGroupSet) {
                userGroup.setGroups(result);
                userGroupService.save(userGroup);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
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
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
    }
}
