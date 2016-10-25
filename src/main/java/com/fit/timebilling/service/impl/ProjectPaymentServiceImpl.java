package com.fit.timebilling.service.impl;

import com.fit.timebilling.service.ProjectPaymentService;
import com.fit.timebilling.domain.ProjectPayment;
import com.fit.timebilling.repository.ProjectPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ProjectPayment.
 */
@Service
@Transactional
public class ProjectPaymentServiceImpl implements ProjectPaymentService{

    private final Logger log = LoggerFactory.getLogger(ProjectPaymentServiceImpl.class);
    
    @Inject
    private ProjectPaymentRepository projectPaymentRepository;

    /**
     * Save a projectPayment.
     *
     * @param projectPayment the entity to save
     * @return the persisted entity
     */
    public ProjectPayment save(ProjectPayment projectPayment) {
        log.debug("Request to save ProjectPayment : {}", projectPayment);
        ProjectPayment result = projectPaymentRepository.save(projectPayment);
        return result;
    }

    /**
     *  Get all the projectPayments.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProjectPayment> findAll() {
        log.debug("Request to get all ProjectPayments");
        List<ProjectPayment> result = projectPaymentRepository.findAll();

        return result;
    }

    /**
     *  Get one projectPayment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProjectPayment findOne(Long id) {
        log.debug("Request to get ProjectPayment : {}", id);
        ProjectPayment projectPayment = projectPaymentRepository.findOne(id);
        return projectPayment;
    }

    /**
     *  Delete the  projectPayment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectPayment : {}", id);
        projectPaymentRepository.delete(id);
    }
}
