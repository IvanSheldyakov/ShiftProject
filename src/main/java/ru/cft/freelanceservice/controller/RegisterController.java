package ru.cft.freelanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.freelanceservice.exceptions.UserIsAlreadyRegisteredException;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;
import ru.cft.freelanceservice.service.RegisterService;

@RestController
@RequestMapping("api/register")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService){
        this.registerService = registerService;
    }

    @PostMapping("/executor")
    public ResponseEntity<?> registerExecutor(@RequestBody ExecutorRegisterDTO executorRegisterDTO) {

        return registerService.registerExecutor(executorRegisterDTO);
    }

    @PostMapping("/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegisterDTO customerRegisterDTO) {

        return registerService.registerCustomer(customerRegisterDTO);
    }

}
