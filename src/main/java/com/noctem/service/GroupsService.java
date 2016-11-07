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

    @Transactional
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        for (UserGroup userGroup : groups.getUserGroups()) {
            userGroup.setGroups(groups);
        }
        return groupsRepository.save(groups);
    }

    @Transactional(readOnly = true)
    public List<Groups> findAll() {
        log.debug("Request to get all Groups");
        List<Groups> result = groupsRepository.findAll();
        return result;
    }
    @Transactional(readOnly = true)
    public List<Groups> findByAuthority() {
        List<Groups> groups = groupsRepository.findByUserIsCurrentUser();
        for (Groups group : groups) {
            log.debug("group:"+group.getName());
        }
        return groups;
    }
    @Transactional(readOnly = true)
    public Groups findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        Groups groups = groupsRepository.findOne(id);
        return groups;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
    }
}
