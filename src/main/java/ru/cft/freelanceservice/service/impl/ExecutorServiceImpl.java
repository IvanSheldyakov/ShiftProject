package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.NoSuchExecutorException;
import ru.cft.freelanceservice.repository.CustomerRepository;
import ru.cft.freelanceservice.repository.ExecutorRepository;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Task;
import ru.cft.freelanceservice.service.ExecutorService;

import java.util.Optional;

@Service
public class ExecutorServiceImpl implements ExecutorService {

    private final ExecutorRepository executorRepository;

    @Autowired
    public ExecutorServiceImpl(ExecutorRepository executorRepository) {
        this.executorRepository = executorRepository;
    }


    @Override
    public Optional<Customer> getCustomer(Long executorId) throws NoSuchExecutorException {
        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
            throw new NoSuchExecutorException();
        }
        Executor executor = executorOptional.get();
        if (executor.getTask() == null) {
            return Optional.empty();
        }
        return Optional.of(executor.getTask().getCustomer());
    }

    @Override
    public Optional<Task> getTask(Long executorId) throws NoSuchExecutorException{
        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
            throw new NoSuchExecutorException();
        }
        Executor executor = executorOptional.get();
        if (executor.getTask() == null) {
            return Optional.empty();
        }

        return Optional.of(executor.getTask());
    }
}
