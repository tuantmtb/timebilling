package com.fit.timebilling.repository;

import com.fit.timebilling.domain.ProjectExpense;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectExpense entity.
 */
@SuppressWarnings("unused")
public interface ProjectExpenseRepository extends JpaRepository<ProjectExpense,Long> {

}
