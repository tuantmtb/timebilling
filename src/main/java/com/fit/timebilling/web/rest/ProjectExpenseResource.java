package com.fit.timebilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fit.timebilling.domain.ProjectExpense;
import com.fit.timebilling.service.ProjectExpenseService;
import com.fit.timebilling.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProjectExpense.
 */
@RestController
@RequestMapping("/api")
public class ProjectExpenseResource {

    private final Logger log = LoggerFactory.getLogger(ProjectExpenseResource.class);
        
    @Inject
    private ProjectExpenseService projectExpenseService;

    /**
     * POST  /project-expenses : Create a new projectExpense.
     *
     * @param projectExpense the projectExpense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectExpense, or with status 400 (Bad Request) if the projectExpense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-expenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectExpense> createProjectExpense(@Valid @RequestBody ProjectExpense projectExpense) throws URISyntaxException {
        log.debug("REST request to save ProjectExpense : {}", projectExpense);
        if (projectExpense.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectExpense", "idexists", "A new projectExpense cannot already have an ID")).body(null);
        }
        ProjectExpense result = projectExpenseService.save(projectExpense);
        return ResponseEntity.created(new URI("/api/project-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectExpense", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-expenses : Updates an existing projectExpense.
     *
     * @param projectExpense the projectExpense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectExpense,
     * or with status 400 (Bad Request) if the projectExpense is not valid,
     * or with status 500 (Internal Server Error) if the projectExpense couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-expenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectExpense> updateProjectExpense(@Valid @RequestBody ProjectExpense projectExpense) throws URISyntaxException {
        log.debug("REST request to update ProjectExpense : {}", projectExpense);
        if (projectExpense.getId() == null) {
            return createProjectExpense(projectExpense);
        }
        ProjectExpense result = projectExpenseService.save(projectExpense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectExpense", projectExpense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-expenses : get all the projectExpenses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectExpenses in body
     */
    @RequestMapping(value = "/project-expenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectExpense> getAllProjectExpenses() {
        log.debug("REST request to get all ProjectExpenses");
        return projectExpenseService.findAll();
    }

    /**
     * GET  /project-expenses/:id : get the "id" projectExpense.
     *
     * @param id the id of the projectExpense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectExpense, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-expenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectExpense> getProjectExpense(@PathVariable Long id) {
        log.debug("REST request to get ProjectExpense : {}", id);
        ProjectExpense projectExpense = projectExpenseService.findOne(id);
        return Optional.ofNullable(projectExpense)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-expenses/:id : delete the "id" projectExpense.
     *
     * @param id the id of the projectExpense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-expenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectExpense(@PathVariable Long id) {
        log.debug("REST request to delete ProjectExpense : {}", id);
        projectExpenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectExpense", id.toString())).build();
    }

}
