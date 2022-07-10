package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.UserIsAlreadyRegisteredException;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;
import ru.cft.freelanceservice.model.SpecializationPriceDTO;
import ru.cft.freelanceservice.repository.PriceRepository;
import ru.cft.freelanceservice.repository.RegisterCustomerRepository;
import ru.cft.freelanceservice.repository.RegisterExecutorRepository;
import ru.cft.freelanceservice.repository.SpecializationRepository;
import ru.cft.freelanceservice.repository.model.Customer;
import ru.cft.freelanceservice.repository.model.Executor;
import ru.cft.freelanceservice.repository.model.Price;
import ru.cft.freelanceservice.repository.model.Specialization;
import ru.cft.freelanceservice.service.RegisterService;

import javax.naming.NoInitialContextException;
import java.util.ArrayList;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final RegisterCustomerRepository customerRepository;
    private final RegisterExecutorRepository executorRepository;

    private final PriceRepository priceRepository;
    private final SpecializationRepository specializationRepository;

    @Autowired
    public RegisterServiceImpl(RegisterCustomerRepository customerRepository,
                               RegisterExecutorRepository executorRepository,
                               PriceRepository priceRepository,
                               SpecializationRepository specializationRepository) {

        this.customerRepository = customerRepository;
        this.executorRepository = executorRepository;
        this.priceRepository = priceRepository;
        this.specializationRepository = specializationRepository;
    }


    @Override
    public ResponseEntity<?> registerCustomer(CustomerRegisterDTO customerRegisterDTO) {
        try {
            checkCustomerForEmptyFields(customerRegisterDTO);
            checkForCustomerRegistration(customerRegisterDTO);
        } catch (NoInitialContextException exception) {
            return new ResponseEntity<>("No data", HttpStatus.BAD_REQUEST);
        } catch (UserIsAlreadyRegisteredException exception) {
            return new ResponseEntity<>("Such user is already registered",HttpStatus.CONFLICT);
        }

        saveCustomer(customerRegisterDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> registerExecutor(ExecutorRegisterDTO executorRegisterDTO) {
        try {
            checkExecutorForEmptyFields(executorRegisterDTO);
            checkForExecutorRegistration(executorRegisterDTO);
        } catch (NoInitialContextException exception) {
            return new ResponseEntity<>("No data", HttpStatus.BAD_REQUEST);
        } catch (UserIsAlreadyRegisteredException exception) {
            return new ResponseEntity<>("Such user is already registered",HttpStatus.CONFLICT);
        }
        saveExecutor(executorRegisterDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkCustomerForEmptyFields(CustomerRegisterDTO dto) throws NoInitialContextException {
        if (dto.getEmail() == null || dto.getName() == null) {
            throw new NoInitialContextException();
        }
    }

    private void checkForCustomerRegistration(CustomerRegisterDTO dto) throws UserIsAlreadyRegisteredException {
        if (!customerRepository.findCustomerByNameAndEmail(dto.getName(), dto.getEmail()).isEmpty()) {
            throw new UserIsAlreadyRegisteredException();
        }
    }

    private void saveCustomer(CustomerRegisterDTO dto) {
        Customer customer = new Customer();
        customer.setFieldsFrom(dto);
        customerRepository.save(customer);
    }


    private void checkExecutorForEmptyFields(ExecutorRegisterDTO dto) throws NoInitialContextException {
        if (dto.getEmail() == null || dto.getName() == null) {throw new NoInitialContextException("there is no email or name");}
        if (dto.getSpecializationsAndPrices().size() < 1) {throw new NoInitialContextException("there is no specializations");}
        for (SpecializationPriceDTO specializationPriceDTO : dto.getSpecializationsAndPrices()) {
            checkSpecializationAndPricesForEmptyFields(specializationPriceDTO);
        }
    }

    private void checkSpecializationAndPricesForEmptyFields(SpecializationPriceDTO dto) throws NoInitialContextException {
        if (dto.getSpecialization() == null || dto.getPrice() == null) {
            throw new NoInitialContextException("there is no specialization or price");
        }
    }

    private void checkForExecutorRegistration(ExecutorRegisterDTO dto) throws UserIsAlreadyRegisteredException {
        if (!executorRepository.findExecutorByNameAndEmail(dto.getName(), dto.getEmail()).isEmpty()) {
            throw new UserIsAlreadyRegisteredException();
        }
    }

    private void saveExecutor(ExecutorRegisterDTO dto) {
        Executor executor = new Executor();
        executor.setFieldsFrom(dto);
        executorRepository.save(executor);
        saveSpecializationsAndPrices(dto,executor);
    }

    private void saveSpecializationsAndPrices(ExecutorRegisterDTO registerDTO, Executor executor) {
        ArrayList<SpecializationPriceDTO> specializationPriceDTOS = registerDTO.getSpecializationsAndPrices();
        for (SpecializationPriceDTO specializationPriceDTO : specializationPriceDTOS) {
            Specialization specialization = new Specialization();
            Price price = new Price();

            price.setPrice(specializationPriceDTO.getPrice());

            specialization.setSpecialization(specializationPriceDTO.getSpecialization());
            specialization.addExecutor(executor);
            specialization.addPrice(price);

            specializationRepository.save(specialization);
            priceRepository.save(price);
        }
    }

}

