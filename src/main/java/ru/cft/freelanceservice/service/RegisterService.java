package ru.cft.freelanceservice.service;

import org.springframework.http.ResponseEntity;
import ru.cft.freelanceservice.exceptions.UserIsAlreadyRegisteredException;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;

public interface RegisterService {
    ResponseEntity<?> registerCustomer(CustomerRegisterDTO customerRegisterDTO);
    ResponseEntity<?> registerExecutor(ExecutorRegisterDTO executorRegisterDTO);
}
