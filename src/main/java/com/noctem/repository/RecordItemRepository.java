package com.noctem.repository;

import com.noctem.domain.RecordItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RecordItem entity.
 */
@SuppressWarnings("unused")
public interface RecordItemRepository extends JpaRepository<RecordItem,Long> {

}
