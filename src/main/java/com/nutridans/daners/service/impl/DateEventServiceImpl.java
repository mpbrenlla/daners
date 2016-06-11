package com.nutridans.daners.service.impl;

import com.nutridans.daners.service.DateEventService;
import com.nutridans.daners.domain.DateEvent;
import com.nutridans.daners.repository.DateEventRepository;
import com.nutridans.daners.repository.search.DateEventSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DateEvent.
 */
@Service
@Transactional
public class DateEventServiceImpl implements DateEventService{

    private final Logger log = LoggerFactory.getLogger(DateEventServiceImpl.class);
    
    @Inject
    private DateEventRepository dateEventRepository;
    
    @Inject
    private DateEventSearchRepository dateEventSearchRepository;
    
    /**
     * Save a dateEvent.
     * 
     * @param dateEvent the entity to save
     * @return the persisted entity
     */
    public DateEvent save(DateEvent dateEvent) {
        log.debug("Request to save DateEvent : {}", dateEvent);
        DateEvent result = dateEventRepository.save(dateEvent);
        dateEventSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the dateEvents.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DateEvent> findAll(Pageable pageable) {
        log.debug("Request to get all DateEvents");
        Page<DateEvent> result = dateEventRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one dateEvent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DateEvent findOne(Long id) {
        log.debug("Request to get DateEvent : {}", id);
        DateEvent dateEvent = dateEventRepository.findOne(id);
        return dateEvent;
    }

    /**
     *  Delete the  dateEvent by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DateEvent : {}", id);
        dateEventRepository.delete(id);
        dateEventSearchRepository.delete(id);
    }

    /**
     * Search for the dateEvent corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DateEvent> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DateEvents for query {}", query);
        return dateEventSearchRepository.search(queryStringQuery(query), pageable);
    }
}
