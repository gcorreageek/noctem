package com.noctem.repository;

import com.noctem.domain.RecordPayment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RecordPayment entity.
 */
@SuppressWarnings("unused")
public interface RecordPaymentRepository extends JpaRepository<RecordPayment,Long> {

    @Query("select recordPayment from RecordPayment recordPayment where recordPayment.user.login = ?#{principal.username}")
    List<RecordPayment> findByUserIsCurrentUser();

    @Query("select recordPayment from RecordPayment recordPayment where recordPayment.record.user.login = ?#{principal.username}")
    List<RecordPayment> findByNotUserIsCurrentUser();

}
