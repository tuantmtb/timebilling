package com.fit.timebilling.web.rest;

import com.fit.timebilling.TimebillingApp;

import com.fit.timebilling.domain.Project;
import com.fit.timebilling.repository.ProjectRepository;
import com.fit.timebilling.service.ProjectService;

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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimebillingApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAA";
    private static final String UPDATED_PRIORITY = "BBBBB";

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_BEGIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BEGIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BEGIN_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_BEGIN);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_END);

    private static final Double DEFAULT_BUDGET = 1D;
    private static final Double UPDATED_BUDGET = 2D;

    private static final String DEFAULT_PO_NUMBER = "AAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBB";

    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    private static final String DEFAULT_ATTACHMENT = "AAAAA";
    private static final String UPDATED_ATTACHMENT = "BBBBB";

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectService projectService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectResource projectResource = new ProjectResource();
        ReflectionTestUtils.setField(projectResource, "projectService", projectService);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
                .name(DEFAULT_NAME)
                .priority(DEFAULT_PRIORITY)
                .status(DEFAULT_STATUS)
                .begin(DEFAULT_BEGIN)
                .end(DEFAULT_END)
                .budget(DEFAULT_BUDGET)
                .poNumber(DEFAULT_PO_NUMBER)
                .note(DEFAULT_NOTE)
                .attachment(DEFAULT_ATTACHMENT);
        return project;
    }

    @Before
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project

        restProjectMockMvc.perform(post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project)))
                .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projects.get(projects.size() - 1);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProject.getBegin()).isEqualTo(DEFAULT_BEGIN);
        assertThat(testProject.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testProject.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testProject.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
        assertThat(testProject.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testProject.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setName(null);

        // Create the Project, which fails.

        restProjectMockMvc.perform(post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project)))
                .andExpect(status().isBadRequest());

        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projects
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN_STR)))
                .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END_STR)))
                .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
                .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
                .andExpect(jsonPath("$.[*].attachment").value(hasItem(DEFAULT_ATTACHMENT.toString())));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN_STR))
            .andExpect(jsonPath("$.end").value(DEFAULT_END_STR))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.poNumber").value(DEFAULT_PO_NUMBER.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.attachment").value(DEFAULT_ATTACHMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        updatedProject
                .name(UPDATED_NAME)
                .priority(UPDATED_PRIORITY)
                .status(UPDATED_STATUS)
                .begin(UPDATED_BEGIN)
                .end(UPDATED_END)
                .budget(UPDATED_BUDGET)
                .poNumber(UPDATED_PO_NUMBER)
                .note(UPDATED_NOTE)
                .attachment(UPDATED_ATTACHMENT);

        restProjectMockMvc.perform(put("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
                .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projects.get(projects.size() - 1);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getBegin()).isEqualTo(UPDATED_BEGIN);
        assertThat(testProject.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testProject.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testProject.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
        assertThat(testProject.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testProject.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
