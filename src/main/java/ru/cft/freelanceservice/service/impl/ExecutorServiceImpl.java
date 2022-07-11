package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.repository.CustomerRepository;
import ru.cft.freelanceservice.repository.ExecutorRepository;
import ru.cft.freelanceservice.repository.model.Executor;
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
    public ResponseEntity<?> getCustomer(Long executorId) {
        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
            return new ResponseEntity<>("no such executor", HttpStatus.BAD_REQUEST);
        }
        Executor executor = executorOptional.get();
        if (executor.getTask() == null) {
            return new ResponseEntity<>("executor doesn't have customer",HttpStatus.OK);
        }
        return new ResponseEntity<>(executor.getTask().getCustomer(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getTask(Long executorId) {
        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
            return new ResponseEntity<>("no such executor", HttpStatus.BAD_REQUEST);
        }
        Executor executor = executorOptional.get();
        if (executor.getTask() == null) {
            return new ResponseEntity<>("executor doesn't have task",HttpStatus.OK);
        }

        return new ResponseEntity<>(executor.getTask(),HttpStatus.OK);
    }
}
