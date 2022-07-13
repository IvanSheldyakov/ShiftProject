package ru.cft.freelanceservice.service;

import org.springframework.http.ResponseEntity;
import ru.cft.freelanceservice.exceptions.CustomerIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.EmptyDTOFieldsException;
import ru.cft.freelanceservice.exceptions.ExecutorIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.NoSuchUserException;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Executor;

public interface RegisterService {
    Customer registerCustomer(CustomerRegisterDTO customerRegisterDTO) throws CustomerIsAlreadyRegisteredException;
    Executor registerExecutor(ExecutorRegisterDTO executorRegisterDTO, Long userId) throws ExecutorIsAlreadyRegisteredException, NoSuchUserException;
}
