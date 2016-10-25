package com.fit.timebilling.service.impl;

import com.fit.timebilling.service.ProjectExpenseService;
import com.fit.timebilling.domain.ProjectExpense;
import com.fit.timebilling.repository.ProjectExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ProjectExpense.
 */
@Service
@Transactional
public class ProjectExpenseServiceImpl implements ProjectExpenseService{

    private final Logger log = LoggerFactory.getLogger(ProjectExpenseServiceImpl.class);
    
    @Inject
    private ProjectExpenseRepository projectExpenseRepository;

    /**
     * Save a projectExpense.
     *
     * @param projectExpense the entity to save
     * @return the persisted entity
     */
    public ProjectExpense save(ProjectExpense projectExpense) {
        log.debug("Request to save ProjectExpense : {}", projectExpense);
        ProjectExpense result = projectExpenseRepository.save(projectExpense);
        return result;
    }

    /**
     *  Get all the projectExpenses.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProjectExpense> findAll() {
        log.debug("Request to get all ProjectExpenses");
        List<ProjectExpense> result = projectExpenseRepository.findAll();

        return result;
    }

    /**
     *  Get one projectExpense by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProjectExpense findOne(Long id) {
        log.debug("Request to get ProjectExpense : {}", id);
        ProjectExpense projectExpense = projectExpenseRepository.findOne(id);
        return projectExpense;
    }

    /**
     *  Delete the  projectExpense by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectExpense : {}", id);
        projectExpenseRepository.delete(id);
    }
}
