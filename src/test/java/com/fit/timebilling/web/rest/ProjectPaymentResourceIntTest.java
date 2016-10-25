package com.fit.timebilling.web.rest;

import com.fit.timebilling.TimebillingApp;

import com.fit.timebilling.domain.ProjectPayment;
import com.fit.timebilling.repository.ProjectPaymentRepository;
import com.fit.timebilling.service.ProjectPaymentService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectPaymentResource REST controller.
 *
 * @see ProjectPaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimebillingApp.class)
public class ProjectPaymentResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE);

    private static final String DEFAULT_METHOD = "AAAAA";
    private static final String UPDATED_METHOD = "BBBBB";

    @Inject
    private ProjectPaymentRepository projectPaymentRepository;

    @Inject
    private ProjectPaymentService projectPaymentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectPaymentMockMvc;

    private ProjectPayment projectPayment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectPaymentResource projectPaymentResource = new ProjectPaymentResource();
        ReflectionTestUtils.setField(projectPaymentResource, "projectPaymentService", projectPaymentService);
        this.restProjectPaymentMockMvc = MockMvcBuilders.standaloneSetup(projectPaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectPayment createEntity(EntityManager em) {
        ProjectPayment projectPayment = new ProjectPayment()
                .amount(DEFAULT_AMOUNT)
                .date(DEFAULT_DATE)
                .method(DEFAULT_METHOD);
        return projectPayment;
    }

    @Before
    public void initTest() {
        projectPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectPayment() throws Exception {
        int databaseSizeBeforeCreate = projectPaymentRepository.findAll().size();

        // Create the ProjectPayment

        restProjectPaymentMockMvc.perform(post("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPayment)))
                .andExpect(status().isCreated());

        // Validate the ProjectPayment in the database
        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeCreate + 1);
        ProjectPayment testProjectPayment = projectPayments.get(projectPayments.size() - 1);
        assertThat(testProjectPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testProjectPayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProjectPayment.getMethod()).isEqualTo(DEFAULT_METHOD);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectPaymentRepository.findAll().size();
        // set the field null
        projectPayment.setAmount(null);

        // Create the ProjectPayment, which fails.

        restProjectPaymentMockMvc.perform(post("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPayment)))
                .andExpect(status().isBadRequest());

        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectPayments() throws Exception {
        // Initialize the database
        projectPaymentRepository.saveAndFlush(projectPayment);

        // Get all the projectPayments
        restProjectPaymentMockMvc.perform(get("/api/project-payments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectPayment.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())));
    }

    @Test
    @Transactional
    public void getProjectPayment() throws Exception {
        // Initialize the database
        projectPaymentRepository.saveAndFlush(projectPayment);

        // Get the projectPayment
        restProjectPaymentMockMvc.perform(get("/api/project-payments/{id}", projectPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectPayment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectPayment() throws Exception {
        // Get the projectPayment
        restProjectPaymentMockMvc.perform(get("/api/project-payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectPayment() throws Exception {
        // Initialize the database
        projectPaymentService.save(projectPayment);

        int databaseSizeBeforeUpdate = projectPaymentRepository.findAll().size();

        // Update the projectPayment
        ProjectPayment updatedProjectPayment = projectPaymentRepository.findOne(projectPayment.getId());
        updatedProjectPayment
                .amount(UPDATED_AMOUNT)
                .date(UPDATED_DATE)
                .method(UPDATED_METHOD);

        restProjectPaymentMockMvc.perform(put("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectPayment)))
                .andExpect(status().isOk());

        // Validate the ProjectPayment in the database
        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeUpdate);
        ProjectPayment testProjectPayment = projectPayments.get(projectPayments.size() - 1);
        assertThat(testProjectPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testProjectPayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProjectPayment.getMethod()).isEqualTo(UPDATED_METHOD);
    }

    @Test
    @Transactional
    public void deleteProjectPayment() throws Exception {
        // Initialize the database
        projectPaymentService.save(projectPayment);

        int databaseSizeBeforeDelete = projectPaymentRepository.findAll().size();

        // Get the projectPayment
        restProjectPaymentMockMvc.perform(delete("/api/project-payments/{id}", projectPayment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
