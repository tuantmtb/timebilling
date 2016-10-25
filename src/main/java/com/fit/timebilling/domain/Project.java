package com.fit.timebilling.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "priority")
    private String priority;

    @Column(name = "status")
    private String status;

    @Column(name = "begin")
    private ZonedDateTime begin;

    @Column(name = "end")
    private ZonedDateTime end;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "note")
    private String note;

    @Column(name = "attachment")
    private String attachment;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public Project priority(String priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public Project status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getBegin() {
        return begin;
    }

    public Project begin(ZonedDateTime begin) {
        this.begin = begin;
        return this;
    }

    public void setBegin(ZonedDateTime begin) {
        this.begin = begin;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public Project end(ZonedDateTime end) {
        this.end = end;
        return this;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Double getBudget() {
        return budget;
    }

    public Project budget(Double budget) {
        this.budget = budget;
        return this;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public Project poNumber(String poNumber) {
        this.poNumber = poNumber;
        return this;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getNote() {
        return note;
    }

    public Project note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAttachment() {
        return attachment;
    }

    public Project attachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Project customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Project employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Category getCategory() {
        return category;
    }

    public Project category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if(project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", priority='" + priority + "'" +
            ", status='" + status + "'" +
            ", begin='" + begin + "'" +
            ", end='" + end + "'" +
            ", budget='" + budget + "'" +
            ", poNumber='" + poNumber + "'" +
            ", note='" + note + "'" +
            ", attachment='" + attachment + "'" +
            '}';
    }
}
