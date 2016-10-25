package com.fit.timebilling.web.rest;

import com.fit.timebilling.TimebillingApp;

import com.fit.timebilling.domain.ProjectExpense;
import com.fit.timebilling.repository.ProjectExpenseRepository;
import com.fit.timebilling.service.ProjectExpenseService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectExpenseResource REST controller.
 *
 * @see ProjectExpenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimebillingApp.class)
public class ProjectExpenseResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    @Inject
    private ProjectExpenseRepository projectExpenseRepository;

    @Inject
    private ProjectExpenseService projectExpenseService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectExpenseMockMvc;

    private ProjectExpense projectExpense;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectExpenseResource projectExpenseResource = new ProjectExpenseResource();
        ReflectionTestUtils.setField(projectExpenseResource, "projectExpenseService", projectExpenseService);
        this.restProjectExpenseMockMvc = MockMvcBuilders.standaloneSetup(projectExpenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectExpense createEntity(EntityManager em) {
        ProjectExpense projectExpense = new ProjectExpense()
                .date(DEFAULT_DATE)
                .description(DEFAULT_DESCRIPTION)
                .code(DEFAULT_CODE)
                .amount(DEFAULT_AMOUNT);
        return projectExpense;
    }

    @Before
    public void initTest() {
        projectExpense = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectExpense() throws Exception {
        int databaseSizeBeforeCreate = projectExpenseRepository.findAll().size();

        // Create the ProjectExpense

        restProjectExpenseMockMvc.perform(post("/api/project-expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectExpense)))
                .andExpect(status().isCreated());

        // Validate the ProjectExpense in the database
        List<ProjectExpense> projectExpenses = projectExpenseRepository.findAll();
        assertThat(projectExpenses).hasSize(databaseSizeBeforeCreate + 1);
        ProjectExpense testProjectExpense = projectExpenses.get(projectExpenses.size() - 1);
        assertThat(testProjectExpense.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProjectExpense.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjectExpense.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjectExpense.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectExpenseRepository.findAll().size();
        // set the field null
        projectExpense.setAmount(null);

        // Create the ProjectExpense, which fails.

        restProjectExpenseMockMvc.perform(post("/api/project-expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectExpense)))
                .andExpect(status().isBadRequest());

        List<ProjectExpense> projectExpenses = projectExpenseRepository.findAll();
        assertThat(projectExpenses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectExpenses() throws Exception {
        // Initialize the database
        projectExpenseRepository.saveAndFlush(projectExpense);

        // Get all the projectExpenses
        restProjectExpenseMockMvc.perform(get("/api/project-expenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectExpense.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    public void getProjectExpense() throws Exception {
        // Initialize the database
        projectExpenseRepository.saveAndFlush(projectExpense);

        // Get the projectExpense
        restProjectExpenseMockMvc.perform(get("/api/project-expenses/{id}", projectExpense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectExpense.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingProjectExpense() throws Exception {
        // Get the projectExpense
        restProjectExpenseMockMvc.perform(get("/api/project-expenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectExpense() throws Exception {
        // Initialize the database
        projectExpenseService.save(projectExpense);

        int databaseSizeBeforeUpdate = projectExpenseRepository.findAll().size();

        // Update the projectExpense
        ProjectExpense updatedProjectExpense = projectExpenseRepository.findOne(projectExpense.getId());
        updatedProjectExpense
                .date(UPDATED_DATE)
                .description(UPDATED_DESCRIPTION)
                .code(UPDATED_CODE)
                .amount(UPDATED_AMOUNT);

        restProjectExpenseMockMvc.perform(put("/api/project-expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectExpense)))
                .andExpect(status().isOk());

        // Validate the ProjectExpense in the database
        List<ProjectExpense> projectExpenses = projectExpenseRepository.findAll();
        assertThat(projectExpenses).hasSize(databaseSizeBeforeUpdate);
        ProjectExpense testProjectExpense = projectExpenses.get(projectExpenses.size() - 1);
        assertThat(testProjectExpense.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProjectExpense.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjectExpense.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjectExpense.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteProjectExpense() throws Exception {
        // Initialize the database
        projectExpenseService.save(projectExpense);

        int databaseSizeBeforeDelete = projectExpenseRepository.findAll().size();

        // Get the projectExpense
        restProjectExpenseMockMvc.perform(delete("/api/project-expenses/{id}", projectExpense.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectExpense> projectExpenses = projectExpenseRepository.findAll();
        assertThat(projectExpenses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
