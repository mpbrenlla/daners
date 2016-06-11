package com.nutridans.daners.web.rest;

import com.nutridans.daners.DanersApp;
import com.nutridans.daners.domain.DateEvent;
import com.nutridans.daners.repository.DateEventRepository;
import com.nutridans.daners.service.DateEventService;
import com.nutridans.daners.repository.search.DateEventSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DateEventResource REST controller.
 *
 * @see DateEventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DanersApp.class)
@WebAppConfiguration
@IntegrationTest
public class DateEventResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_BREAKFAST = "AAAAA";
    private static final String UPDATED_BREAKFAST = "BBBBB";
    private static final String DEFAULT_BRUNCH = "AAAAA";
    private static final String UPDATED_BRUNCH = "BBBBB";
    private static final String DEFAULT_LUNCH = "AAAAA";
    private static final String UPDATED_LUNCH = "BBBBB";
    private static final String DEFAULT_TEA = "AAAAA";
    private static final String UPDATED_TEA = "BBBBB";
    private static final String DEFAULT_DINNER = "AAAAA";
    private static final String UPDATED_DINNER = "BBBBB";

    @Inject
    private DateEventRepository dateEventRepository;

    @Inject
    private DateEventService dateEventService;

    @Inject
    private DateEventSearchRepository dateEventSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDateEventMockMvc;

    private DateEvent dateEvent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DateEventResource dateEventResource = new DateEventResource();
        ReflectionTestUtils.setField(dateEventResource, "dateEventService", dateEventService);
        this.restDateEventMockMvc = MockMvcBuilders.standaloneSetup(dateEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dateEventSearchRepository.deleteAll();
        dateEvent = new DateEvent();
        dateEvent.setDate(DEFAULT_DATE);
        dateEvent.setBreakfast(DEFAULT_BREAKFAST);
        dateEvent.setBrunch(DEFAULT_BRUNCH);
        dateEvent.setLunch(DEFAULT_LUNCH);
        dateEvent.setTea(DEFAULT_TEA);
        dateEvent.setDinner(DEFAULT_DINNER);
    }

    @Test
    @Transactional
    public void createDateEvent() throws Exception {
        int databaseSizeBeforeCreate = dateEventRepository.findAll().size();

        // Create the DateEvent

        restDateEventMockMvc.perform(post("/api/date-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dateEvent)))
                .andExpect(status().isCreated());

        // Validate the DateEvent in the database
        List<DateEvent> dateEvents = dateEventRepository.findAll();
        assertThat(dateEvents).hasSize(databaseSizeBeforeCreate + 1);
        DateEvent testDateEvent = dateEvents.get(dateEvents.size() - 1);
        assertThat(testDateEvent.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDateEvent.getBreakfast()).isEqualTo(DEFAULT_BREAKFAST);
        assertThat(testDateEvent.getBrunch()).isEqualTo(DEFAULT_BRUNCH);
        assertThat(testDateEvent.getLunch()).isEqualTo(DEFAULT_LUNCH);
        assertThat(testDateEvent.getTea()).isEqualTo(DEFAULT_TEA);
        assertThat(testDateEvent.getDinner()).isEqualTo(DEFAULT_DINNER);

        // Validate the DateEvent in ElasticSearch
        DateEvent dateEventEs = dateEventSearchRepository.findOne(testDateEvent.getId());
        assertThat(dateEventEs).isEqualToComparingFieldByField(testDateEvent);
    }

    @Test
    @Transactional
    public void getAllDateEvents() throws Exception {
        // Initialize the database
        dateEventRepository.saveAndFlush(dateEvent);

        // Get all the dateEvents
        restDateEventMockMvc.perform(get("/api/date-events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dateEvent.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].breakfast").value(hasItem(DEFAULT_BREAKFAST.toString())))
                .andExpect(jsonPath("$.[*].brunch").value(hasItem(DEFAULT_BRUNCH.toString())))
                .andExpect(jsonPath("$.[*].lunch").value(hasItem(DEFAULT_LUNCH.toString())))
                .andExpect(jsonPath("$.[*].tea").value(hasItem(DEFAULT_TEA.toString())))
                .andExpect(jsonPath("$.[*].dinner").value(hasItem(DEFAULT_DINNER.toString())));
    }

    @Test
    @Transactional
    public void getDateEvent() throws Exception {
        // Initialize the database
        dateEventRepository.saveAndFlush(dateEvent);

        // Get the dateEvent
        restDateEventMockMvc.perform(get("/api/date-events/{id}", dateEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dateEvent.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.breakfast").value(DEFAULT_BREAKFAST.toString()))
            .andExpect(jsonPath("$.brunch").value(DEFAULT_BRUNCH.toString()))
            .andExpect(jsonPath("$.lunch").value(DEFAULT_LUNCH.toString()))
            .andExpect(jsonPath("$.tea").value(DEFAULT_TEA.toString()))
            .andExpect(jsonPath("$.dinner").value(DEFAULT_DINNER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDateEvent() throws Exception {
        // Get the dateEvent
        restDateEventMockMvc.perform(get("/api/date-events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDateEvent() throws Exception {
        // Initialize the database
        dateEventService.save(dateEvent);

        int databaseSizeBeforeUpdate = dateEventRepository.findAll().size();

        // Update the dateEvent
        DateEvent updatedDateEvent = new DateEvent();
        updatedDateEvent.setId(dateEvent.getId());
        updatedDateEvent.setDate(UPDATED_DATE);
        updatedDateEvent.setBreakfast(UPDATED_BREAKFAST);
        updatedDateEvent.setBrunch(UPDATED_BRUNCH);
        updatedDateEvent.setLunch(UPDATED_LUNCH);
        updatedDateEvent.setTea(UPDATED_TEA);
        updatedDateEvent.setDinner(UPDATED_DINNER);

        restDateEventMockMvc.perform(put("/api/date-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDateEvent)))
                .andExpect(status().isOk());

        // Validate the DateEvent in the database
        List<DateEvent> dateEvents = dateEventRepository.findAll();
        assertThat(dateEvents).hasSize(databaseSizeBeforeUpdate);
        DateEvent testDateEvent = dateEvents.get(dateEvents.size() - 1);
        assertThat(testDateEvent.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDateEvent.getBreakfast()).isEqualTo(UPDATED_BREAKFAST);
        assertThat(testDateEvent.getBrunch()).isEqualTo(UPDATED_BRUNCH);
        assertThat(testDateEvent.getLunch()).isEqualTo(UPDATED_LUNCH);
        assertThat(testDateEvent.getTea()).isEqualTo(UPDATED_TEA);
        assertThat(testDateEvent.getDinner()).isEqualTo(UPDATED_DINNER);

        // Validate the DateEvent in ElasticSearch
        DateEvent dateEventEs = dateEventSearchRepository.findOne(testDateEvent.getId());
        assertThat(dateEventEs).isEqualToComparingFieldByField(testDateEvent);
    }

    @Test
    @Transactional
    public void deleteDateEvent() throws Exception {
        // Initialize the database
        dateEventService.save(dateEvent);

        int databaseSizeBeforeDelete = dateEventRepository.findAll().size();

        // Get the dateEvent
        restDateEventMockMvc.perform(delete("/api/date-events/{id}", dateEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean dateEventExistsInEs = dateEventSearchRepository.exists(dateEvent.getId());
        assertThat(dateEventExistsInEs).isFalse();

        // Validate the database is empty
        List<DateEvent> dateEvents = dateEventRepository.findAll();
        assertThat(dateEvents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDateEvent() throws Exception {
        // Initialize the database
        dateEventService.save(dateEvent);

        // Search the dateEvent
        restDateEventMockMvc.perform(get("/api/_search/date-events?query=id:" + dateEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dateEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].breakfast").value(hasItem(DEFAULT_BREAKFAST.toString())))
            .andExpect(jsonPath("$.[*].brunch").value(hasItem(DEFAULT_BRUNCH.toString())))
            .andExpect(jsonPath("$.[*].lunch").value(hasItem(DEFAULT_LUNCH.toString())))
            .andExpect(jsonPath("$.[*].tea").value(hasItem(DEFAULT_TEA.toString())))
            .andExpect(jsonPath("$.[*].dinner").value(hasItem(DEFAULT_DINNER.toString())));
    }
}
