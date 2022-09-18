package ru.cft.freelanceservice.service;


import ru.cft.freelanceservice.exceptions.NoSuchExecutorException;
import ru.cft.freelanceservice.exceptions.NoSuchTaskException;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Task;

import java.util.List;
import java.util.Optional;

public interface ExecutorService {

    Optional<Customer> getCustomer(Long executorId) throws NoSuchExecutorException;
    Optional<Task> getTask(Long executorId) throws NoSuchExecutorException;
    List<Task> getOpenTasks();

    Optional<Task> finishTask(Long executorId) throws NoSuchExecutorException, NoSuchTaskException;

}
