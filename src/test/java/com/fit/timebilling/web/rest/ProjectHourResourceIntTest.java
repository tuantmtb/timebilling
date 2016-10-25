package com.fit.timebilling.web.rest;

import com.fit.timebilling.TimebillingApp;

import com.fit.timebilling.domain.ProjectHour;
import com.fit.timebilling.repository.ProjectHourRepository;
import com.fit.timebilling.service.ProjectHourService;

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
 * Test class for the ProjectHourResource REST controller.
 *
 * @see ProjectHourResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimebillingApp.class)
public class ProjectHourResourceIntTest {

    private static final LocalDate DEFAULT_DATE_WORKED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_WORKED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_BILLABLE_HOUR = 1;
    private static final Integer UPDATED_BILLABLE_HOUR = 2;

    private static final String DEFAULT_WORK_CODE = "AAAAA";
    private static final String UPDATED_WORK_CODE = "BBBBB";

    @Inject
    private ProjectHourRepository projectHourRepository;

    @Inject
    private ProjectHourService projectHourService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectHourMockMvc;

    private ProjectHour projectHour;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectHourResource projectHourResource = new ProjectHourResource();
        ReflectionTestUtils.setField(projectHourResource, "projectHourService", projectHourService);
        this.restProjectHourMockMvc = MockMvcBuilders.standaloneSetup(projectHourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectHour createEntity(EntityManager em) {
        ProjectHour projectHour = new ProjectHour()
                .dateWorked(DEFAULT_DATE_WORKED)
                .description(DEFAULT_DESCRIPTION)
                .billableHour(DEFAULT_BILLABLE_HOUR)
                .workCode(DEFAULT_WORK_CODE);
        return projectHour;
    }

    @Before
    public void initTest() {
        projectHour = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectHour() throws Exception {
        int databaseSizeBeforeCreate = projectHourRepository.findAll().size();

        // Create the ProjectHour

        restProjectHourMockMvc.perform(post("/api/project-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectHour)))
                .andExpect(status().isCreated());

        // Validate the ProjectHour in the database
        List<ProjectHour> projectHours = projectHourRepository.findAll();
        assertThat(projectHours).hasSize(databaseSizeBeforeCreate + 1);
        ProjectHour testProjectHour = projectHours.get(projectHours.size() - 1);
        assertThat(testProjectHour.getDateWorked()).isEqualTo(DEFAULT_DATE_WORKED);
        assertThat(testProjectHour.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjectHour.getBillableHour()).isEqualTo(DEFAULT_BILLABLE_HOUR);
        assertThat(testProjectHour.getWorkCode()).isEqualTo(DEFAULT_WORK_CODE);
    }

    @Test
    @Transactional
    public void getAllProjectHours() throws Exception {
        // Initialize the database
        projectHourRepository.saveAndFlush(projectHour);

        // Get all the projectHours
        restProjectHourMockMvc.perform(get("/api/project-hours?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectHour.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateWorked").value(hasItem(DEFAULT_DATE_WORKED.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].billableHour").value(hasItem(DEFAULT_BILLABLE_HOUR)))
                .andExpect(jsonPath("$.[*].workCode").value(hasItem(DEFAULT_WORK_CODE.toString())));
    }

    @Test
    @Transactional
    public void getProjectHour() throws Exception {
        // Initialize the database
        projectHourRepository.saveAndFlush(projectHour);

        // Get the projectHour
        restProjectHourMockMvc.perform(get("/api/project-hours/{id}", projectHour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectHour.getId().intValue()))
            .andExpect(jsonPath("$.dateWorked").value(DEFAULT_DATE_WORKED.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.billableHour").value(DEFAULT_BILLABLE_HOUR))
            .andExpect(jsonPath("$.workCode").value(DEFAULT_WORK_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectHour() throws Exception {
        // Get the projectHour
        restProjectHourMockMvc.perform(get("/api/project-hours/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectHour() throws Exception {
        // Initialize the database
        projectHourService.save(projectHour);

        int databaseSizeBeforeUpdate = projectHourRepository.findAll().size();

        // Update the projectHour
        ProjectHour updatedProjectHour = projectHourRepository.findOne(projectHour.getId());
        updatedProjectHour
                .dateWorked(UPDATED_DATE_WORKED)
                .description(UPDATED_DESCRIPTION)
                .billableHour(UPDATED_BILLABLE_HOUR)
                .workCode(UPDATED_WORK_CODE);

        restProjectHourMockMvc.perform(put("/api/project-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectHour)))
                .andExpect(status().isOk());

        // Validate the ProjectHour in the database
        List<ProjectHour> projectHours = projectHourRepository.findAll();
        assertThat(projectHours).hasSize(databaseSizeBeforeUpdate);
        ProjectHour testProjectHour = projectHours.get(projectHours.size() - 1);
        assertThat(testProjectHour.getDateWorked()).isEqualTo(UPDATED_DATE_WORKED);
        assertThat(testProjectHour.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjectHour.getBillableHour()).isEqualTo(UPDATED_BILLABLE_HOUR);
        assertThat(testProjectHour.getWorkCode()).isEqualTo(UPDATED_WORK_CODE);
    }

    @Test
    @Transactional
    public void deleteProjectHour() throws Exception {
        // Initialize the database
        projectHourService.save(projectHour);

        int databaseSizeBeforeDelete = projectHourRepository.findAll().size();

        // Get the projectHour
        restProjectHourMockMvc.perform(delete("/api/project-hours/{id}", projectHour.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectHour> projectHours = projectHourRepository.findAll();
        assertThat(projectHours).hasSize(databaseSizeBeforeDelete - 1);
    }
}
