package ru.cft.freelanceservice.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cft.freelanceservice.exceptions.ExecutorIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.NoSuchExecutorException;
import ru.cft.freelanceservice.exceptions.NoSuchTaskException;
import ru.cft.freelanceservice.exceptions.NoSuchUserException;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;
import ru.cft.freelanceservice.model.MessageResponse;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Task;
import ru.cft.freelanceservice.service.ExecutorService;
import ru.cft.freelanceservice.service.RegisterService;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Api
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
        if (executorId == null) {
            return new ResponseEntity<>(new MessageResponse("No data"), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Customer> customerOptional = executorService.getCustomer(executorId);
            if (customerOptional.isEmpty()) {
                return new ResponseEntity<>(new MessageResponse("Executor doesn't have customer"),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(customerOptional.get(),HttpStatus.OK);
            }
        } catch (NoSuchExecutorException e) {
            return new ResponseEntity<>(new MessageResponse("No such executor"), HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    @PostMapping(value = "/finish/task", params = "executorId")
    public ResponseEntity<?> finishTask(@RequestParam Long executorId) {

        try {
            Optional<Task> taskOptional = executorService.finishTask(executorId);
            return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
        } catch (NoSuchExecutorException e) {
            return new ResponseEntity<>(new MessageResponse("No such executor"), HttpStatus.BAD_REQUEST);
        } catch (NoSuchTaskException e) {
            return new ResponseEntity<>(new MessageResponse("Executor doesn't have task"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    @GetMapping(value = "/get/open/tasks")
    public ResponseEntity<?> getOpenTasks() {
        return new ResponseEntity<>(executorService.getOpenTasks(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    @GetMapping(value = "/get/task", params = "executorId")
    public ResponseEntity<?> getTask(@RequestParam Long executorId) {
        if (executorId == null) {
            return new ResponseEntity<>(new MessageResponse("no data"), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Task> taskOptional = executorService.getTask(executorId);
            if (taskOptional.isEmpty()) {
                return new ResponseEntity<>("executor doesn't have customer",HttpStatus.OK);
            } else {
                return new ResponseEntity<>(taskOptional.get(),HttpStatus.OK);
            }
        } catch (NoSuchExecutorException e) {
            return new ResponseEntity<>(new MessageResponse("no such executor"), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/register",params = "userId")
    public ResponseEntity<?> registerExecutor(@RequestBody ExecutorRegisterDTO executorRegisterDTO, @RequestParam Long userId) {

        if (userId == null || checkNull(executorRegisterDTO)) {
            return new ResponseEntity<>(new MessageResponse("no data"), HttpStatus.BAD_REQUEST);
        }

        try {
            Executor executor = registerService.registerExecutor(executorRegisterDTO, userId);
            return new ResponseEntity<>(new MessageResponse("executor is successfully registered with id "
                    + executor.getId()),HttpStatus.OK);
        } catch (ExecutorIsAlreadyRegisteredException e) {
            return new ResponseEntity<>(new MessageResponse("such executor is already registered"), HttpStatus.BAD_REQUEST);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(new MessageResponse("no such user registered"), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkNull(ExecutorRegisterDTO dto) {
        if (Objects.isNull(dto)) {return true;}
        return dto.getSpecializationsAndPrices() == null || (dto.getSpecializationsAndPrices().size() < 1); //Stream.of(dto.getUsername(), dto.getEmail()).anyMatch(Objects::isNull);

    }
}
