package ru.cft.freelanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cft.freelanceservice.exceptions.ExecutorIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.NoSuchUserException;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;
import ru.cft.freelanceservice.model.MessageResponse;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.service.ExecutorService;
import ru.cft.freelanceservice.service.RegisterService;

@RestController
@RequestMapping("api/executor")
public class ExecutorController {

    private final ExecutorService executorService;
    private final RegisterService registerService;

    @Autowired
    public ExecutorController(ExecutorService executorService,
                              RegisterService registerService) {
        this.executorService = executorService;
        this.registerService = registerService;
    }

    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    @GetMapping(value = "/get/customer", params = "executorId")
    public ResponseEntity<?> getCustomer(@RequestParam Long executorId) {
        return executorService.getCustomer(executorId);
    }

    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    @GetMapping(value = "/get/task", params = "executorId")
    public ResponseEntity<?> getTask(@RequestParam Long executorId) {
        return executorService.getTask(executorId);
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> registerExecutor(@RequestBody ExecutorRegisterDTO executorRegisterDTO) {

        try {
            Executor executor = registerService.registerExecutor(executorRegisterDTO);
            return new ResponseEntity<>(new MessageResponse("executor is successfully registered with id "
                    + executor.getId()),HttpStatus.OK);
        } catch (ExecutorIsAlreadyRegisteredException e) {
            return new ResponseEntity<>(new MessageResponse("such executor is already registered"), HttpStatus.BAD_REQUEST);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(new MessageResponse("no such user registered"), HttpStatus.BAD_REQUEST);
        }
    }
}
