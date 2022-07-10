package ru.cft.freelanceservice.repository.model;

import javax.persistence.*;

@Entity
@Table(name = "PRICES")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String price;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private Specialization specialization;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
