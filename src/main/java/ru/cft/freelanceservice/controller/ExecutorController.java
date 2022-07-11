package ru.cft.freelanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.freelanceservice.service.ExecutorService;

@RestController
@RequestMapping("api/executor")
public class ExecutorController {

    private final ExecutorService executorService;

    @Autowired
    public ExecutorController(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @GetMapping(value = "/get/customer", params = "executorId")
    public ResponseEntity<?> getCustomer(@RequestParam Long executorId) {
        return executorService.getCustomer(executorId);
    }

    @GetMapping(value = "/get/task", params = "executorId")
    public ResponseEntity<?> getTask(@RequestParam Long executorId) {
        return executorService.getTask(executorId);
    }

}
