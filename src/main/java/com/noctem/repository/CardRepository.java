package com.noctem.repository;

import com.noctem.domain.Card;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Card entity.
 */
@SuppressWarnings("unused")
public interface CardRepository extends JpaRepository<Card,Long> {

    @Query("select card from Card card where card.user.login = ?#{principal.username}")
    List<Card> findByUserIsCurrentUser();

}
