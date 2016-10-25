package com.fit.timebilling.service.impl;

import com.fit.timebilling.service.ProjectHourService;
import com.fit.timebilling.domain.ProjectHour;
import com.fit.timebilling.repository.ProjectHourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ProjectHour.
 */
@Service
@Transactional
public class ProjectHourServiceImpl implements ProjectHourService{

    private final Logger log = LoggerFactory.getLogger(ProjectHourServiceImpl.class);
    
    @Inject
    private ProjectHourRepository projectHourRepository;

    /**
     * Save a projectHour.
     *
     * @param projectHour the entity to save
     * @return the persisted entity
     */
    public ProjectHour save(ProjectHour projectHour) {
        log.debug("Request to save ProjectHour : {}", projectHour);
        ProjectHour result = projectHourRepository.save(projectHour);
        return result;
    }

    /**
     *  Get all the projectHours.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProjectHour> findAll() {
        log.debug("Request to get all ProjectHours");
        List<ProjectHour> result = projectHourRepository.findAll();

        return result;
    }

    /**
     *  Get one projectHour by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProjectHour findOne(Long id) {
        log.debug("Request to get ProjectHour : {}", id);
        ProjectHour projectHour = projectHourRepository.findOne(id);
        return projectHour;
    }

    /**
     *  Delete the  projectHour by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectHour : {}", id);
        projectHourRepository.delete(id);
    }
}
