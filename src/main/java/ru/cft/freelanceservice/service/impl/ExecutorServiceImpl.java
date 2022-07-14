package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.NoSuchExecutorException;
import ru.cft.freelanceservice.exceptions.NoSuchTaskException;
import ru.cft.freelanceservice.model.TaskStatus;
import ru.cft.freelanceservice.repository.CustomerRepository;
import ru.cft.freelanceservice.repository.ExecutorRepository;
import ru.cft.freelanceservice.repository.TaskRepository;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Task;
import ru.cft.freelanceservice.service.ExecutorService;

import java.util.List;
import java.util.Optional;

@Service
public class ExecutorServiceImpl implements ExecutorService {

    private final ExecutorRepository executorRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ExecutorServiceImpl(ExecutorRepository executorRepository, TaskRepository taskRepository) {
        this.executorRepository = executorRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public Optional<Customer> getCustomer(Long executorId) throws NoSuchExecutorException {
        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
            throw new NoSuchExecutorException();
        }
        Executor executor = executorOptional.get();
        if (executor.getTask() == null) {
            return Optional.empty();
        }
        return Optional.of(executor.getTask().getCustomer());
    }

    @Override
    public Optional<Task> getTask(Long executorId) throws NoSuchExecutorException{
        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
            throw new NoSuchExecutorException();
        }
        Executor executor = executorOptional.get();
        if (executor.getTask() == null) {
            return Optional.empty();
        }

        return Optional.of(executor.getTask());
    }

    @Override
    public List<Task> getOpenTasks() {
        return taskRepository.findAllByStatus(TaskStatus.OPEN);
    }

    @Override
    public Optional<Task> finishTask(Long executorId) throws NoSuchExecutorException, NoSuchTaskException {

        Optional<Executor> executorOptional = executorRepository.findById(executorId);
        if (executorOptional.isEmpty()) {
          throw new NoSuchExecutorException();
        }
        Executor executor = executorOptional.get();

        if (executor.getTask() == null) {
            throw new NoSuchTaskException();
        }
        Task task = executor.getTask();
        task.setStatus(TaskStatus.FINISHED);
        taskRepository.save(task);
        return Optional.of(task);
    }
}
