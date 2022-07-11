package ru.cft.freelanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.freelanceservice.repository.model.Executor;

import java.util.Optional;

@Repository
public interface ExecutorRepository extends JpaRepository<Executor,Long> {
    Executor save(Executor executor);
    Optional<Executor> findExecutorByNameAndEmail(String name, String email);
    Optional<Executor> findById(Long id);
    void deleteByNameAndEmail(String name, String email);

}
