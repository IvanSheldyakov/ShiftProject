package ru.cft.freelanceservice.repository.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CUSTOMERS")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Set<Task> tasks = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "customer")
    private User user;

    public void addTask(Task task) {
        tasks.add(task);
        task.setCustomer(this);
    }

    public void removeTask(Task task) {
        task.setCustomer(null);
        tasks.remove(task);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
