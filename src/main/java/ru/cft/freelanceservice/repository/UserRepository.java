package ru.cft.freelanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.freelanceservice.repository.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean existsByUsernameAndEmail(String username, String email);
    Optional<User> findByUsernameAndAndEmail(String username,String email);
}
