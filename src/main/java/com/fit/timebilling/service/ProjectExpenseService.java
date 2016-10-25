package com.fit.timebilling.service;

import com.fit.timebilling.domain.ProjectExpense;

import java.util.List;

/**
 * Service Interface for managing ProjectExpense.
 */
public interface ProjectExpenseService {

    /**
     * Save a projectExpense.
     *
     * @param projectExpense the entity to save
     * @return the persisted entity
     */
    ProjectExpense save(ProjectExpense projectExpense);

    /**
     *  Get all the projectExpenses.
     *  
     *  @return the list of entities
     */
    List<ProjectExpense> findAll();

    /**
     *  Get the "id" projectExpense.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectExpense findOne(Long id);

    /**
     *  Delete the "id" projectExpense.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
