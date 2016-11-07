package com.noctem.repository;

import com.noctem.domain.Record;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Record entity.
 */
@SuppressWarnings("unused")
public interface RecordRepository extends JpaRepository<Record,Long> {

    @Query("select record from Record record where record.user.login = ?#{principal.username}")
    List<Record> findByUserIsCurrentUser();

}
