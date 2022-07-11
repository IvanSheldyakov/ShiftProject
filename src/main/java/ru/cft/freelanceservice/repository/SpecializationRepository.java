package ru.cft.freelanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.freelanceservice.repository.model.Specialization;

import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization,Long> {
    Specialization save(Specialization specialization);
    Optional<Specialization> findBySpecialization(String specialization);
}
