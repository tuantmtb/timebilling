package com.fit.timebilling.service;

import com.fit.timebilling.domain.ProjectPayment;

import java.util.List;

/**
 * Service Interface for managing ProjectPayment.
 */
public interface ProjectPaymentService {

    /**
     * Save a projectPayment.
     *
     * @param projectPayment the entity to save
     * @return the persisted entity
     */
    ProjectPayment save(ProjectPayment projectPayment);

    /**
     *  Get all the projectPayments.
     *  
     *  @return the list of entities
     */
    List<ProjectPayment> findAll();

    /**
     *  Get the "id" projectPayment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectPayment findOne(Long id);

    /**
     *  Delete the "id" projectPayment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
