package ru.cft.freelanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskIdExecutorIdDTO;
import ru.cft.freelanceservice.service.CustomerService;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService service) {
        this.customerService = service;
    }

    @PostMapping(value = "/create/task", params = "customerId")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO, @RequestParam long customerId) {
        return customerService.createTask(taskDTO,customerId);
    }

    @GetMapping(value = "/find/specialization", params = "specialization")
    public ResponseEntity<?> findExecutorsBySpecialization(@RequestParam String specialization) {
        return customerService.findExecutorsBySpecialization(specialization);
    }

    @PostMapping("/choose/executor")
    public ResponseEntity<?> chooseExecutorForTask(@RequestBody TaskIdExecutorIdDTO dto) {
        return customerService.chooseExecutorForTask(dto);
    }

    @DeleteMapping(value = "/delete/task",params = "id")
    public ResponseEntity<?> deleteTask(@RequestParam long id ) {
        return customerService.deleteTask(id);
    }
}
