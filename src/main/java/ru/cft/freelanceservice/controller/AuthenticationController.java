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
import ru.cft.freelanceservice.model.*;
import ru.cft.freelanceservice.repository.RoleRepository;
import ru.cft.freelanceservice.repository.UserRepository;
import ru.cft.freelanceservice.repository.model.Role;
import ru.cft.freelanceservice.repository.model.User;
import ru.cft.freelanceservice.security.jwt.JwtTokenProvider;
import ru.cft.freelanceservice.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService,
                                    BCryptPasswordEncoder encoder,
                                    RoleRepository roleRepository,
                                    UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest requestDTO) {
        try{

            String username = requestDTO.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,requestDTO.getPassword()));
            Optional<User> userOptional = userService.findByUsername(username);
            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException("user with username " + username + " not found");
            }
            User user = userOptional.get();
            List<Role> roles = user.getRoles().stream().toList();
            String token = jwtTokenProvider.createToken(username,user.getRoles().stream().toList());


            return new ResponseEntity<>(new JwtResponse(token,user.getId(),username,user.getEmail(),roles), HttpStatus.OK);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new MessageResponse("Invalid username or password"),HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        if (userService.existByUsername(request.getUsername())) {
            return new ResponseEntity<>
                    (new MessageResponse("user with such username is already exist"),HttpStatus.BAD_REQUEST);
        }
        if (userService.existByEmail(request.getEmail())) {
            return new ResponseEntity<>
                    (new MessageResponse("user with such email is already exist"),HttpStatus.BAD_REQUEST);
        }
        User user = new User(request.getUsername(),encoder.encode(request.getPassword()),request.getEmail());

        List<String> reqRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "customer":
                        Role customerRole = roleRepository
                                .findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error, Role CUSTOMER is not found"));
                        roles.add(customerRole);

                        break;
                    case "executor":
                        Role executorRole = roleRepository
                                .findByName(ERole.ROLE_EXECUTOR)
                                .orElseThrow(() -> new RuntimeException("Error, Role EXECUTOR is not found"));
                        roles.add(executorRole);

                        break;

                    default:
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("user registered successfully"),HttpStatus.OK);

    }


}
