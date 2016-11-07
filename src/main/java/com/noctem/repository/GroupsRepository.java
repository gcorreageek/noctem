package com.noctem.repository;

import com.noctem.domain.Groups;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Groups entity.
 */
@SuppressWarnings("unused")
public interface GroupsRepository extends JpaRepository<Groups,Long> {

    @Query("select groups from Groups groups where groups.user.login = ?#{principal.username}")
    List<Groups> findByUserIsCurrentUser();

}
