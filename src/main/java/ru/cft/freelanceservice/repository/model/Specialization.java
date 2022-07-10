package ru.cft.freelanceservice.repository.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SPECIALIZATIONS")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String specialization;

    @ManyToMany
    @JoinTable(name = "specialization_executor",
    joinColumns = @JoinColumn(name = "special_id"),
    inverseJoinColumns = @JoinColumn(name = "executor_id"))
    private Set<Executor> executors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @JsonIgnore
    @OneToMany(mappedBy = "specialization")
    private Set<Price> prices = new HashSet<>();

    public void addPrice(Price price) {
        prices.add(price);
        price.setSpecialization(this);
    }

    public void removePrice(Price price) {
        price.setSpecialization(null);
        prices.remove(price);
    }

    public void addExecutor(Executor executor) {
        executors.add(executor);
        executor.getSpecializations().add(this);
    }

    public void removeExecutor(Executor executor) {
        executor.getSpecializations().remove(this);
        executors.remove(executor);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Set<Executor> getExecutors() {
        return executors;
    }

    public void setExecutors(Set<Executor> executors) {
        this.executors = executors;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
