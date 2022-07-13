package ru.cft.freelanceservice.service;


import org.springframework.http.ResponseEntity;
import ru.cft.freelanceservice.exceptions.NoSuchExecutorException;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Task;

import java.util.Optional;

public interface ExecutorService {

    Optional<Customer> getCustomer(Long executorId) throws NoSuchExecutorException;
    Optional<Task> getTask(Long executorId) throws NoSuchExecutorException;
}
