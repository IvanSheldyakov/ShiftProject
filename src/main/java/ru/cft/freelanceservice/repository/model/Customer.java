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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String username;
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Set<Task> tasks = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_role_id", referencedColumnName = "id")
    private Role role;



    public void addTask(Task task) {
        tasks.add(task);
        task.setCustomer(this);
    }

    public void removeTask(Task task) {
        task.setCustomer(null);
        tasks.remove(task);
    }



    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
