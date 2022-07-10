package ru.cft.freelanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import ru.cft.freelanceservice.repository.model.Customer;

import java.util.Optional;

@Repository
public interface RegisterCustomerRepository extends JpaRepository<Customer,Long> {



    Customer save(Customer customer);

    Optional<Customer> findCustomerByNameAndEmail(String name, String email);

}
