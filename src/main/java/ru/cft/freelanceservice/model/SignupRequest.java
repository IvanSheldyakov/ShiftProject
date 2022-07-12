package ru.cft.freelanceservice.model;

import lombok.Data;

import java.util.List;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private List<String> roles;
}
