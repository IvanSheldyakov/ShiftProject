package ru.cft.freelanceservice.model;


import lombok.Data;
import ru.cft.freelanceservice.repository.model.Role;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<Role> roles;

    public JwtResponse(String token, Long id, String username, String email, List<Role> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
