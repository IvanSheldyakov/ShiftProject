package ru.cft.freelanceservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.freelanceservice.repository.model.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Task save(Task task);
    Optional<Task> findById(Long id);
}
