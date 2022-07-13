package ru.cft.freelanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.freelanceservice.exceptions.*;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskIdExecutorIdDTO;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Task;
import ru.cft.freelanceservice.service.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService service) {
        this.customerService = service;
    }

    @PostMapping(value = "/create/task", params = "customerId")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO, @RequestParam Long customerId) {

        if (checkForEmptyFields(taskDTO) || customerId == null) {
            return new ResponseEntity<>("no data", HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<Task> task = customerService.createTask(taskDTO, customerId);
            return new ResponseEntity<>(task.get(),HttpStatus.OK);
        } catch (NoSuchCustomerException e) {
            return new ResponseEntity<>("No such customer", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/find/specialization", params = "specialization")
    public ResponseEntity<?> findExecutorsBySpecialization(@RequestParam String specialization) {
        if (specialization == null) {
            return new ResponseEntity<>("no data", HttpStatus.BAD_REQUEST);
        }
        try {
            List<Executor> executors = customerService.findAllExecutorsBySpecialization(specialization);
            return new ResponseEntity<>(executors, HttpStatus.OK);
        } catch (NoSuchSpecializationException e) {
            return new ResponseEntity<>("no such specialization", HttpStatus.OK);
        }
    }

    @PostMapping("/choose/executor")
    public ResponseEntity<?> chooseExecutorForTask(@RequestBody TaskIdExecutorIdDTO dto) {
        if (checkForEmptyFields(dto)) {
            return new ResponseEntity<>("no data", HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Executor> executorOptional = customerService.chooseExecutorForTask(dto);
            return new ResponseEntity<>(executorOptional.get(), HttpStatus.OK);
        } catch (NoSuchExecutorException e) {
            return new ResponseEntity<>("No such executor", HttpStatus.BAD_REQUEST);
        } catch (NoSuchTaskException e) {
            return new ResponseEntity<>("no such task", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/delete/task",params = "id")
    public ResponseEntity<?> deleteTask(@RequestParam Long id ) {
        if (id == null) {
            return new ResponseEntity<>("no data", HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Task> taskOptional = customerService.deleteTask(id);
            return new ResponseEntity<>(taskOptional.get(),HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return new ResponseEntity<>("no such task", HttpStatus.BAD_REQUEST);
        }

    }

    private boolean checkForEmptyFields(TaskDTO dto)  {
        return dto.getName() == null || dto.getDescription() == null || dto.getSpecializations().size() < 1;
    }

    private boolean checkForEmptyFields(TaskIdExecutorIdDTO dto)  {
        return dto.getExecutorId() == null || dto.getTaskId() == null;
    }
}
