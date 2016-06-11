package com.nutridans.daners.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nutridans.daners.domain.DateEvent;
import com.nutridans.daners.service.DateEventService;
import com.nutridans.daners.web.rest.util.HeaderUtil;
import com.nutridans.daners.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DateEvent.
 */
@RestController
@RequestMapping("/api")
public class DateEventResource {

    private final Logger log = LoggerFactory.getLogger(DateEventResource.class);
        
    @Inject
    private DateEventService dateEventService;
    
    /**
     * POST  /date-events : Create a new dateEvent.
     *
     * @param dateEvent the dateEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dateEvent, or with status 400 (Bad Request) if the dateEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/date-events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DateEvent> createDateEvent(@RequestBody DateEvent dateEvent) throws URISyntaxException {
        log.debug("REST request to save DateEvent : {}", dateEvent);
        if (dateEvent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dateEvent", "idexists", "A new dateEvent cannot already have an ID")).body(null);
        }
        DateEvent result = dateEventService.save(dateEvent);
        return ResponseEntity.created(new URI("/api/date-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dateEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /date-events : Updates an existing dateEvent.
     *
     * @param dateEvent the dateEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dateEvent,
     * or with status 400 (Bad Request) if the dateEvent is not valid,
     * or with status 500 (Internal Server Error) if the dateEvent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/date-events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DateEvent> updateDateEvent(@RequestBody DateEvent dateEvent) throws URISyntaxException {
        log.debug("REST request to update DateEvent : {}", dateEvent);
        if (dateEvent.getId() == null) {
            return createDateEvent(dateEvent);
        }
        DateEvent result = dateEventService.save(dateEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dateEvent", dateEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /date-events : get all the dateEvents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dateEvents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/date-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DateEvent>> getAllDateEvents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DateEvents");
        Page<DateEvent> page = dateEventService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/date-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /date-events/:id : get the "id" dateEvent.
     *
     * @param id the id of the dateEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dateEvent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/date-events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DateEvent> getDateEvent(@PathVariable Long id) {
        log.debug("REST request to get DateEvent : {}", id);
        DateEvent dateEvent = dateEventService.findOne(id);
        return Optional.ofNullable(dateEvent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /date-events/:id : delete the "id" dateEvent.
     *
     * @param id the id of the dateEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/date-events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDateEvent(@PathVariable Long id) {
        log.debug("REST request to delete DateEvent : {}", id);
        dateEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dateEvent", id.toString())).build();
    }

    /**
     * SEARCH  /_search/date-events?query=:query : search for the dateEvent corresponding
     * to the query.
     *
     * @param query the query of the dateEvent search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/date-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DateEvent>> searchDateEvents(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DateEvents for query {}", query);
        Page<DateEvent> page = dateEventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/date-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
