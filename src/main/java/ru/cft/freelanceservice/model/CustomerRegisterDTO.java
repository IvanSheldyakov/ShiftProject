package ru.cft.freelanceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerRegisterDTO {
    private String username;
    private String email;
}


