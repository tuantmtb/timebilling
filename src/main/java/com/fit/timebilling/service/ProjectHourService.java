package com.fit.timebilling.service;

import com.fit.timebilling.domain.ProjectHour;

import java.util.List;

/**
 * Service Interface for managing ProjectHour.
 */
public interface ProjectHourService {

    /**
     * Save a projectHour.
     *
     * @param projectHour the entity to save
     * @return the persisted entity
     */
    ProjectHour save(ProjectHour projectHour);

    /**
     *  Get all the projectHours.
     *  
     *  @return the list of entities
     */
    List<ProjectHour> findAll();

    /**
     *  Get the "id" projectHour.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectHour findOne(Long id);

    /**
     *  Delete the "id" projectHour.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
