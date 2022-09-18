package ru.cft.freelanceservice.service;


import ru.cft.freelanceservice.exceptions.*;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskIdExecutorIdDTO;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Task;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<Task> createTask(TaskDTO taskDTO, Long userId) throws NoSuchCustomerException;
    List<Executor> findAllExecutorsBySpecialization(String specialization) throws NoSuchSpecializationException;
    Optional<Executor> chooseExecutorForTask(TaskIdExecutorIdDTO taskIdExecutorIdDTO)
            throws NoSuchExecutorException, NoSuchTaskException,
            TaskIsAlreadyFinished, ExecutorAlreadyHasTaskException, ExecutorHasDifferentSpecializationsComparedToTaskException;

    Optional<Task> deleteTask(Long taskId) throws NoSuchTaskException;

}
