package com.fit.timebilling.repository;

import com.fit.timebilling.domain.ProjectPayment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectPayment entity.
 */
@SuppressWarnings("unused")
public interface ProjectPaymentRepository extends JpaRepository<ProjectPayment,Long> {

}
