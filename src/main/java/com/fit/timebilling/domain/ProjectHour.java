package com.fit.timebilling.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProjectHour.
 */
@Entity
@Table(name = "project_hour")
public class ProjectHour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_worked")
    private LocalDate dateWorked;

    @Column(name = "description")
    private String description;

    @Column(name = "billable_hour")
    private Integer billableHour;

    @Column(name = "work_code")
    private String workCode;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateWorked() {
        return dateWorked;
    }

    public ProjectHour dateWorked(LocalDate dateWorked) {
        this.dateWorked = dateWorked;
        return this;
    }

    public void setDateWorked(LocalDate dateWorked) {
        this.dateWorked = dateWorked;
    }

    public String getDescription() {
        return description;
    }

    public ProjectHour description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBillableHour() {
        return billableHour;
    }

    public ProjectHour billableHour(Integer billableHour) {
        this.billableHour = billableHour;
        return this;
    }

    public void setBillableHour(Integer billableHour) {
        this.billableHour = billableHour;
    }

    public String getWorkCode() {
        return workCode;
    }

    public ProjectHour workCode(String workCode) {
        this.workCode = workCode;
        return this;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public Employee getEmployee() {
        return employee;
    }

    public ProjectHour employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectHour projectHour = (ProjectHour) o;
        if(projectHour.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectHour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectHour{" +
            "id=" + id +
            ", dateWorked='" + dateWorked + "'" +
            ", description='" + description + "'" +
            ", billableHour='" + billableHour + "'" +
            ", workCode='" + workCode + "'" +
            '}';
    }
}
