package ru.cft.freelanceservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EXECUTORS")
public class Executor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "executors")
    private Set<Specialization> specializations = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setFieldsFrom(ExecutorRegisterDTO dto) {
        name = dto.getName();
        email = dto.getEmail();
    }
}
