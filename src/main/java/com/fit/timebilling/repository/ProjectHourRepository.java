package com.fit.timebilling.repository;

import com.fit.timebilling.domain.ProjectHour;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectHour entity.
 */
@SuppressWarnings("unused")
public interface ProjectHourRepository extends JpaRepository<ProjectHour,Long> {

}
