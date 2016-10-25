package com.fit.timebilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fit.timebilling.domain.ProjectPayment;
import com.fit.timebilling.service.ProjectPaymentService;
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
 * REST controller for managing ProjectPayment.
 */
@RestController
@RequestMapping("/api")
public class ProjectPaymentResource {

    private final Logger log = LoggerFactory.getLogger(ProjectPaymentResource.class);
        
    @Inject
    private ProjectPaymentService projectPaymentService;

    /**
     * POST  /project-payments : Create a new projectPayment.
     *
     * @param projectPayment the projectPayment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectPayment, or with status 400 (Bad Request) if the projectPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-payments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectPayment> createProjectPayment(@Valid @RequestBody ProjectPayment projectPayment) throws URISyntaxException {
        log.debug("REST request to save ProjectPayment : {}", projectPayment);
        if (projectPayment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectPayment", "idexists", "A new projectPayment cannot already have an ID")).body(null);
        }
        ProjectPayment result = projectPaymentService.save(projectPayment);
        return ResponseEntity.created(new URI("/api/project-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectPayment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-payments : Updates an existing projectPayment.
     *
     * @param projectPayment the projectPayment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectPayment,
     * or with status 400 (Bad Request) if the projectPayment is not valid,
     * or with status 500 (Internal Server Error) if the projectPayment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-payments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectPayment> updateProjectPayment(@Valid @RequestBody ProjectPayment projectPayment) throws URISyntaxException {
        log.debug("REST request to update ProjectPayment : {}", projectPayment);
        if (projectPayment.getId() == null) {
            return createProjectPayment(projectPayment);
        }
        ProjectPayment result = projectPaymentService.save(projectPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectPayment", projectPayment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-payments : get all the projectPayments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectPayments in body
     */
    @RequestMapping(value = "/project-payments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectPayment> getAllProjectPayments() {
        log.debug("REST request to get all ProjectPayments");
        return projectPaymentService.findAll();
    }

    /**
     * GET  /project-payments/:id : get the "id" projectPayment.
     *
     * @param id the id of the projectPayment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectPayment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-payments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectPayment> getProjectPayment(@PathVariable Long id) {
        log.debug("REST request to get ProjectPayment : {}", id);
        ProjectPayment projectPayment = projectPaymentService.findOne(id);
        return Optional.ofNullable(projectPayment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-payments/:id : delete the "id" projectPayment.
     *
     * @param id the id of the projectPayment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-payments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectPayment(@PathVariable Long id) {
        log.debug("REST request to delete ProjectPayment : {}", id);
        projectPaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectPayment", id.toString())).build();
    }

}
