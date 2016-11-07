package com.noctem.repository;

import com.noctem.domain.Event;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select event from Event event where event.user.login = ?#{principal.username}")
    List<Event> findByUserIsCurrentUser();

}
