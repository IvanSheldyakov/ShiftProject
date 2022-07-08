package ru.cft.freelanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.freelanceservice.repository.model.SampleEntity;
import ru.cft.freelanceservice.service.RegisterService;

import java.util.List;

@RestController
@RequestMapping("api/register")
public class RegisterController {

    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService){
        this.registerService = registerService;
    }

    @PostMapping("/performer")
    public ResponseEntity<String> registerPerformer() {
        //some work TODO finish
        return null;
    }

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer() {
        // some workTODO finish
        return null;
    }

}
