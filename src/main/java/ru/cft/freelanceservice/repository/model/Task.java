package ru.cft.freelanceservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TASKS")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private Set<Executor> executors = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private Set<Specialization> specializations = new HashSet<>();


    public void addExecutor(Executor executor) {
        executors.add(executor);
        executor.setTask(this);
    }

    public void removeExecutor(Executor executor) {
        executors.remove(executor);
        executor.setTask(null);
    }

    public void removeExecutors(List<Executor> toRemove) {
        toRemove.forEach(executors::remove);
        for (Executor executor : toRemove) {
            executor.setTask(null);
        }
    }


    public void addSpecialization(Specialization specialization) {
        specializations.add(specialization);
        specialization.setTask(this);
    }

    public void removeSpecialization(Specialization specialization) {
        specializations.remove(specialization);
        specialization.setTask(null);
    }

    public void removeSpecializations(List<Specialization> toRemove) {
        toRemove.forEach(specializations::remove);
        for (Specialization specialization : toRemove) {
            specialization.setTask(null);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Executor> getExecutors() {
        return executors;
    }

    public void setExecutors(Set<Executor> executors) {
        this.executors = executors;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }
    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public void setFieldsFrom(TaskDTO dto) {
        name = dto.getName();
        description = dto.getDescription();
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
