package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.CustomerIsAlreadyRegisteredException;
import ru.cft.freelanceservice.model.*;
import ru.cft.freelanceservice.repository.CustomerRepository;
import ru.cft.freelanceservice.repository.RoleRepository;
import ru.cft.freelanceservice.repository.UserRepository;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Role;
import ru.cft.freelanceservice.repository.model.User;
import ru.cft.freelanceservice.security.jwt.JwtTokenProvider;
import ru.cft.freelanceservice.service.AuthenticationService;
import ru.cft.freelanceservice.service.RegisterService;
import ru.cft.freelanceservice.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final RegisterService registerService;

    private final CustomerRepository customerRepository;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtTokenProvider jwtTokenProvider,
                                     UserService userService,
                                     BCryptPasswordEncoder encoder,
                                     RoleRepository roleRepository,
                                     UserRepository userRepository,
                                     RegisterService registerService,
                                     CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.registerService = registerService;
        this.customerRepository = customerRepository;
    }

    @Override
    public User register(SignupRequest request) {
        User user = new User(request.getUsername(), encoder.encode(request.getPassword()), request.getEmail());
        userRepository.save(user);
        setRoles(user, request.getRoles());
        return userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest request) throws AuthenticationException {
        String username = request.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("user with username " + username + " not found");
        }
        User user = userOptional.get();
        List<Role> roles = user.getRoles().stream().toList();
        String token = jwtTokenProvider.createToken(username, user.getRoles().stream().toList());
        return new JwtResponse(token, user.getId(), username, user.getEmail(), roles);
    }

    private void setRoles(User user, List<String> reqRoles) { //DEFAULT CUSTOMER
        Set<Role> roles = new HashSet<>();
        registerCustomer(user,roles);
        user.setRoles(roles);
    }

    private void registerCustomer(User user, Set<Role> roles)  {
        Optional<Role> customerRoleOptional = roleRepository
                .findByName(ERole.ROLE_CUSTOMER);
        if (!customerRoleOptional.isPresent()) {
            userRepository.deleteById(user.getId());
            throw new RuntimeException("Error, Role CUSTOMER is not found");
        }
        Role customerRole = customerRoleOptional.get();
        roles.add(customerRole);
        try {
            Customer customer = registerService.registerCustomer(new CustomerRegisterDTO(user.getUsername(), user.getEmail()));
            customerRole.addCustomer(customer);
            roleRepository.save(customerRole);

            user.setCustomerId(customer.getId());
            customer.setUserId(user.getId());
            customerRepository.save(customer);
        } catch (CustomerIsAlreadyRegisteredException e) {
            //impossible way because there is validation before
        }
    }


}
