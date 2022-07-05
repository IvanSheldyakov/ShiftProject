package ru.cft.yellowrubberduck.repository.model;

import javax.persistence.*;

@Entity
@Table(name = "sample")
public class SampleEntity {

    @Id
    @GeneratedValue
    public long id;

    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;
}
