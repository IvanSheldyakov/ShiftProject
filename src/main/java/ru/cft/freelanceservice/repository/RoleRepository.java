package ru.cft.freelanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.freelanceservice.model.ERole;
import ru.cft.freelanceservice.repository.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(ERole name);


}
