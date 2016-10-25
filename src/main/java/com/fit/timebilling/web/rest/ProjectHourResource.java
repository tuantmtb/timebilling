package com.fit.timebilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fit.timebilling.domain.ProjectHour;
import com.fit.timebilling.service.ProjectHourService;
import com.fit.timebilling.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * REST controller for managing ProjectHour.
 */
@RestController
@RequestMapping("/api")
public class ProjectHourResource {

    private final Logger log = LoggerFactory.getLogger(ProjectHourResource.class);
        
    @Inject
    private ProjectHourService projectHourService;

    /**
     * POST  /project-hours : Create a new projectHour.
     *
     * @param projectHour the projectHour to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectHour, or with status 400 (Bad Request) if the projectHour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-hours",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectHour> createProjectHour(@RequestBody ProjectHour projectHour) throws URISyntaxException {
        log.debug("REST request to save ProjectHour : {}", projectHour);
        if (projectHour.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectHour", "idexists", "A new projectHour cannot already have an ID")).body(null);
        }
        ProjectHour result = projectHourService.save(projectHour);
        return ResponseEntity.created(new URI("/api/project-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectHour", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-hours : Updates an existing projectHour.
     *
     * @param projectHour the projectHour to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectHour,
     * or with status 400 (Bad Request) if the projectHour is not valid,
     * or with status 500 (Internal Server Error) if the projectHour couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-hours",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectHour> updateProjectHour(@RequestBody ProjectHour projectHour) throws URISyntaxException {
        log.debug("REST request to update ProjectHour : {}", projectHour);
        if (projectHour.getId() == null) {
            return createProjectHour(projectHour);
        }
        ProjectHour result = projectHourService.save(projectHour);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectHour", projectHour.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-hours : get all the projectHours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectHours in body
     */
    @RequestMapping(value = "/project-hours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectHour> getAllProjectHours() {
        log.debug("REST request to get all ProjectHours");
        return projectHourService.findAll();
    }

    /**
     * GET  /project-hours/:id : get the "id" projectHour.
     *
     * @param id the id of the projectHour to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectHour, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-hours/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectHour> getProjectHour(@PathVariable Long id) {
        log.debug("REST request to get ProjectHour : {}", id);
        ProjectHour projectHour = projectHourService.findOne(id);
        return Optional.ofNullable(projectHour)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-hours/:id : delete the "id" projectHour.
     *
     * @param id the id of the projectHour to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-hours/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectHour(@PathVariable Long id) {
        log.debug("REST request to delete ProjectHour : {}", id);
        projectHourService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectHour", id.toString())).build();
    }

}
