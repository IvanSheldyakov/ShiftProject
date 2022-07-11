package ru.cft.freelanceservice.service;


import org.springframework.http.ResponseEntity;

public interface ExecutorService {

    ResponseEntity<?> getCustomer(Long executorId);
    ResponseEntity<?> getTask(Long executorId);
}
