package ru.cft.freelanceservice.service;

import ru.cft.freelanceservice.repository.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    void delete(Long id);

    boolean existByUsername(String username);
    boolean existByEmail(String email);
}
