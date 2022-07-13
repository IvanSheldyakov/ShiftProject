package ru.cft.freelanceservice.service;


import org.springframework.security.core.AuthenticationException;
import ru.cft.freelanceservice.model.JwtResponse;
import ru.cft.freelanceservice.model.LoginRequest;
import ru.cft.freelanceservice.model.SignupRequest;
import ru.cft.freelanceservice.repository.model.User;

public interface AuthenticationService {

    User register(SignupRequest request);
    JwtResponse login(LoginRequest request) throws AuthenticationException;
}
