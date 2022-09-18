package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.*;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskIdExecutorIdDTO;
import ru.cft.freelanceservice.model.TaskStatus;
import ru.cft.freelanceservice.repository.CustomerRepository;
import ru.cft.freelanceservice.repository.ExecutorRepository;
import ru.cft.freelanceservice.repository.SpecializationRepository;
import ru.cft.freelanceservice.repository.TaskRepository;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Specialization;
import ru.cft.freelanceservice.repository.model.Task;
import ru.cft.freelanceservice.service.CustomerService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TaskRepository taskRepository;
    private final SpecializationRepository specializationRepository;
    private final ExecutorRepository executorRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               TaskRepository taskRepository,
                               SpecializationRepository specializationRepository,
                               ExecutorRepository executorRepository) {
        this.customerRepository = customerRepository;
        this.taskRepository = taskRepository;
        this.specializationRepository = specializationRepository;
        this.executorRepository = executorRepository;
    }

    @Override
    public Optional<Task> createTask(TaskDTO taskDTO, Long customerId) throws NoSuchCustomerException {

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new NoSuchCustomerException();
        }

        Task task = createNewTask(taskDTO);
        customer.get().addTask(task);
        customerRepository.save(customer.get());

        return Optional.of(task);
    }

    @Override
    public List<Executor> findAllExecutorsBySpecialization(String specialization) throws NoSuchSpecializationException {
        List<Specialization> specializationEntities = specializationRepository.findBySpecialization(specialization);
        if (specializationEntities.isEmpty()) {
            throw new NoSuchSpecializationException();
        }
        Set<Executor> executors = new HashSet<>();
        for (Specialization entity: specializationEntities) {
            executors.addAll(entity.getExecutors());
        }

        return executors.stream().toList();
    }

    @Override
    public Optional<Executor> chooseExecutorForTask(TaskIdExecutorIdDTO taskIdExecutorIdDTO)
            throws
            NoSuchExecutorException,
            NoSuchTaskException,
            TaskIsAlreadyFinished,
            ExecutorAlreadyHasTaskException,
            ExecutorHasDifferentSpecializationsComparedToTaskException{


        Optional<Executor> executorOptional = executorRepository.findById(taskIdExecutorIdDTO.getExecutorId());
        if (executorOptional.isEmpty()) {
            throw new NoSuchExecutorException();
        }

        Executor executor = executorOptional.get();
        if (executor.getTask() != null) {
            throw new ExecutorAlreadyHasTaskException();
        }

        Optional<Task> taskOptional = taskRepository.findById(taskIdExecutorIdDTO.getTaskId());
        if (taskOptional.isEmpty()) {
            throw new NoSuchTaskException();
        }

        Task task = taskOptional.get();
        if (task.getStatus() == TaskStatus.FINISHED) {
            throw new TaskIsAlreadyFinished();
        }

        Set<String> intersection = task.getSpecializations().stream().map(Specialization::getSpecialization)
                .collect(Collectors.toSet());

        intersection.retainAll(executor.getSpecializations().stream().map(Specialization::getSpecialization)
                .collect(Collectors.toSet()));
        if (intersection.size() == 0) {
            throw new ExecutorHasDifferentSpecializationsComparedToTaskException();
        }

        task.addExecutor(executor);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(task);
        return Optional.of(executor);
    }

    @Override
    public Optional<Task> deleteTask(Long taskId) throws NoSuchTaskException{

        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {

            throw new NoSuchTaskException();
        }

        Task task = taskOptional.get();

        deleteTaskFromCustomer(task);
        deleteSpecializationOf(task);
        deleteExecutorsOf(task);

        taskRepository.delete(task);
        return taskOptional;
    }

    private Task createNewTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setFieldsFrom(taskDTO);
        task.setStatus(TaskStatus.OPEN);
        task = taskRepository.save(task);
        addSpecializations(taskDTO, task);
        return taskRepository.save(task);
    }

    private void addSpecializations(TaskDTO taskDTO, Task task) {
        for (String specialization : taskDTO.getSpecializations()) {
            task.addSpecialization(createNewSpecialization(specialization));
        }
    }

    private Specialization createNewSpecialization(String specialization) {
        Specialization newSpecialization = new Specialization();
        newSpecialization.setSpecialization(specialization);
        return specializationRepository.save(newSpecialization);
    }



    private void deleteTaskFromCustomer(Task task) {
        Customer customer = task.getCustomer();
        customer.removeTask(task);
        customerRepository.save(customer);

    }

    private void deleteSpecializationOf(Task task) {
        List<Specialization> specializations = task.getSpecializations().stream().toList();
        task.removeSpecializations(specializations);
        specializationRepository.saveAll(specializations);
    }

    private void deleteExecutorsOf(Task task) {
        List<Executor> executors = task.getExecutors().stream().toList();
        task.removeExecutors(executors);
        executorRepository.saveAll(executors);
    }
}
