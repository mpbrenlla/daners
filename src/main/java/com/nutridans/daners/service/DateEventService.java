package com.nutridans.daners.service;

import com.nutridans.daners.domain.DateEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing DateEvent.
 */
public interface DateEventService {

    /**
     * Save a dateEvent.
     * 
     * @param dateEvent the entity to save
     * @return the persisted entity
     */
    DateEvent save(DateEvent dateEvent);

    /**
     *  Get all the dateEvents.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DateEvent> findAll(Pageable pageable);

    /**
     *  Get the "id" dateEvent.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    DateEvent findOne(Long id);

    /**
     *  Delete the "id" dateEvent.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the dateEvent corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<DateEvent> search(String query, Pageable pageable);
}
