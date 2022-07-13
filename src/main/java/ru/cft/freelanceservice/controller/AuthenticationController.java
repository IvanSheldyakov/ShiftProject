package ru.cft.freelanceservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.freelanceservice.exceptions.CustomerIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.EmptyDTOFieldsException;
import ru.cft.freelanceservice.model.*;

import ru.cft.freelanceservice.repository.model.User;
import ru.cft.freelanceservice.security.jwt.JwtTokenProvider;
import ru.cft.freelanceservice.service.AuthenticationService;
import ru.cft.freelanceservice.service.RegisterService;
import ru.cft.freelanceservice.service.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest requestDTO) {
        try {

            JwtResponse response = authenticationService.login(requestDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new MessageResponse("Invalid username or password"), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        if (userService.existByUsername(request.getUsername())) {
            return new ResponseEntity<>
                    (new MessageResponse("user with such username is already exist"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existByEmail(request.getEmail())) {
            return new ResponseEntity<>
                    (new MessageResponse("user with such email is already exist"), HttpStatus.BAD_REQUEST);
        }


        User user = authenticationService.register(request);

        return new ResponseEntity<>
                (new MessageResponse("Customer registered successfully with id-" + user.getCustomerId()), HttpStatus.OK);
    }



}
