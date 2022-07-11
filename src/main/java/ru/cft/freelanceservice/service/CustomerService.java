package ru.cft.freelanceservice.service;


import org.springframework.http.ResponseEntity;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskIdExecutorIdDTO;

public interface CustomerService {

    ResponseEntity<?> createTask(TaskDTO taskDTO, Long customerId);
    ResponseEntity<?> findExecutorsBySpecialization(String specialization);
    ResponseEntity<?> chooseExecutorForTask(TaskIdExecutorIdDTO taskIdExecutorIdDTO);

    ResponseEntity<?> deleteTask(Long taskId);

}
