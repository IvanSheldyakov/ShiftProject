package ru.cft.freelanceservice.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.cft.freelanceservice.model.ERole;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<Customer> customers  = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<Executor> executors  = new HashSet<>();

    public void addCustomer(Customer customer) {
        customers.add(customer);
        customer.setRole(this);
    }

    public void removeCustomer(Customer customer) {
        customer.setRole(null);
        customers.remove(customer);
    }

    public void addExecutor(Executor executor) {
        executors.add(executor);
        executor.setRole(this);
    }

    public void removeExecutor(Executor executor) {
        executor.setRole(null);
        executors.remove(executor);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
