package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.EmptyDTOFieldsException;
import ru.cft.freelanceservice.model.TaskDTO;
import ru.cft.freelanceservice.model.TaskIdExecutorIdDTO;
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
    public ResponseEntity<?> createTask(TaskDTO taskDTO, Long customerId) {

        try {
            checkForEmptyFields(taskDTO);
        } catch (EmptyDTOFieldsException e) {
            return new ResponseEntity<>("no data", HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            return new ResponseEntity<>("No such user", HttpStatus.BAD_REQUEST);
        }

        Task task = createNewTask(taskDTO);
        customer.get().addTask(task);
        customerRepository.save(customer.get());

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findExecutorsBySpecialization(String specialization) {
        Optional<Specialization> specializationEntity = specializationRepository.findBySpecialization(specialization);
        if (specializationEntity.isEmpty()) {
            return new ResponseEntity<>("no such specialization", HttpStatus.OK);
        }
        return new ResponseEntity<>(specializationEntity.get().getExecutors(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> chooseExecutorForTask(TaskIdExecutorIdDTO taskIdExecutorIdDTO) {

        try {
            checkForEmptyFields(taskIdExecutorIdDTO);
        } catch (EmptyDTOFieldsException exception) {
            return new ResponseEntity<>("No data", HttpStatus.BAD_REQUEST);
        }

        Optional<Executor> executor = executorRepository.findById(taskIdExecutorIdDTO.getExecutorId());
        if (executor.isEmpty()) {
            return new ResponseEntity<>("No such executor", HttpStatus.BAD_REQUEST);
        }
        Optional<Task> task = taskRepository.findById(taskIdExecutorIdDTO.getTaskId());
        if (task.isEmpty()) {
            return new ResponseEntity<>("No such task", HttpStatus.BAD_REQUEST);
        }


        task.get().addExecutor(executor.get());
        taskRepository.save(task.get());
        return new ResponseEntity<>(executor, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteTask(Long taskId) {
        if (taskId == null) {
            return new ResponseEntity<>("no data", HttpStatus.BAD_REQUEST);
        }

        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            return new ResponseEntity<>("no such task", HttpStatus.BAD_REQUEST);
        }

        Task task = taskOptional.get();

        deleteTaskFromCustomer(task);
        deleteSpecializationOf(task);
        deleteExecutorsOf(task);

        taskRepository.delete(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    private Task createNewTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setFieldsFrom(taskDTO);
        task = taskRepository.save(task);
        addSpecializations(taskDTO, task);
        return taskRepository.save(task);
    }

    private void addSpecializations(TaskDTO taskDTO, Task task) {
        for (String specialization : taskDTO.getSpecializations()) {
            Optional<Specialization> specializationEntity = specializationRepository.findBySpecialization(specialization);

            if (specializationEntity.isPresent()) {
                task.addSpecialization(specializationEntity.get());
            } else {
                task.addSpecialization(createNewSpecialization(specialization));
            }
        }
    }

    private Specialization createNewSpecialization(String specialization) {
        Specialization newSpecialization = new Specialization();
        newSpecialization.setSpecialization(specialization);
        return specializationRepository.save(newSpecialization);
    }

    private void checkForEmptyFields(TaskDTO dto) throws EmptyDTOFieldsException {
        if (dto.getName() == null || dto.getDescription() == null || dto.getSpecializations().size() < 1) {
            throw new EmptyDTOFieldsException();
        }
    }

    private void checkForEmptyFields(TaskIdExecutorIdDTO dto) throws EmptyDTOFieldsException {
        if (dto.getExecutorId() == null || dto.getTaskId() == null) {
            throw new EmptyDTOFieldsException();
        }
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
