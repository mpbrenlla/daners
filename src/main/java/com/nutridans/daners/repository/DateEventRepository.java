package com.nutridans.daners.repository;

import com.nutridans.daners.domain.DateEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DateEvent entity.
 */
@SuppressWarnings("unused")
public interface DateEventRepository extends JpaRepository<DateEvent,Long> {

    @Query("select dateEvent from DateEvent dateEvent where dateEvent.owner.login = ?#{principal.username}")
    List<DateEvent> findByOwnerIsCurrentUser();

}
