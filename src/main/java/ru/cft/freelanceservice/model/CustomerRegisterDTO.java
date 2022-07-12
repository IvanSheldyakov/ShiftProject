package ru.cft.freelanceservice.model;

import lombok.Data;

@Data
public class CustomerRegisterDTO {
    private String name;
    private String email;
    private String firstName;
    private String secondName;
    private String password;
}


