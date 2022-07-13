package ru.cft.freelanceservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.cft.freelanceservice.exceptions.CustomerIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.ExecutorIsAlreadyRegisteredException;
import ru.cft.freelanceservice.exceptions.NoSuchUserException;
import ru.cft.freelanceservice.model.CustomerRegisterDTO;
import ru.cft.freelanceservice.model.ERole;
import ru.cft.freelanceservice.model.ExecutorRegisterDTO;
import ru.cft.freelanceservice.model.SpecializationPriceDTO;
import ru.cft.freelanceservice.repository.*;
import ru.cft.freelanceservice.repository.model.*;
import ru.cft.freelanceservice.service.RegisterService;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class RegisterServiceImpl implements RegisterService {

    private final CustomerRepository customerRepository;
    private final ExecutorRepository executorRepository;

    private final PriceRepository priceRepository;
    private final SpecializationRepository specializationRepository;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RegisterServiceImpl(CustomerRepository customerRepository,
                               ExecutorRepository executorRepository,
                               PriceRepository priceRepository,
                               SpecializationRepository specializationRepository,
                               RoleRepository roleRepository,
                               UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.executorRepository = executorRepository;
        this.priceRepository = priceRepository;
        this.specializationRepository = specializationRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }





    @Override
    public Customer registerCustomer(CustomerRegisterDTO customerRegisterDTO) throws CustomerIsAlreadyRegisteredException{


        if (checkForCustomerRegistration(customerRegisterDTO)) {
            throw new CustomerIsAlreadyRegisteredException();
        }

        return saveCustomer(customerRegisterDTO);
    }

    @Override
    public Executor registerExecutor(ExecutorRegisterDTO executorRegisterDTO) throws ExecutorIsAlreadyRegisteredException, NoSuchUserException {

        if (!checkForUserRegistration(executorRegisterDTO)) {
            throw new NoSuchUserException();
        }

        if (checkForExecutorRegistration(executorRegisterDTO)) {
            throw new ExecutorIsAlreadyRegisteredException();
        }

        return saveExecutor(executorRegisterDTO);

    }



    private boolean checkNull(CustomerRegisterDTO dto) {
        if (Objects.isNull(dto)) {return true;}
        return Stream.of(dto.getUsername(), dto.getEmail()).anyMatch(Objects::isNull);

    }

    private boolean checkForCustomerRegistration(CustomerRegisterDTO dto) {
        return customerRepository.existsByUsernameAndEmail(dto.getUsername(), dto.getEmail());
    }

    private boolean checkForUserRegistration(ExecutorRegisterDTO dto) {
        return userRepository.existsByUsernameAndEmail(dto.getUsername(), dto.getEmail());
    }

   private Customer saveCustomer(CustomerRegisterDTO dto) {
        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setEmail(dto.getEmail());
        customerRepository.save(customer);
        return customer;
    }


     private boolean checkForExecutorRegistration(ExecutorRegisterDTO dto) {
        return executorRepository.existsByUsernameAndEmail(dto.getUsername(), dto.getEmail());
    }

    private Executor saveExecutor(ExecutorRegisterDTO dto)  {
        Executor executor = init(dto);
        addExecutorToUser(executor);
        saveSpecializationsAndPrices(dto,executor);
        return executor;
    }

    private Executor init(ExecutorRegisterDTO dto) {
        Executor executor = new Executor();
        executor.setUsername(dto.getUsername());
        executor.setEmail(dto.getEmail());
        return executorRepository.save(executor);
    }

    private Role addExecutorToRole(Executor executor) {
        Role role = roleRepository.findByName(ERole.ROLE_EXECUTOR).get();
        role.addExecutor(executor);
        return roleRepository.save(role);
    }

    private void addExecutorToUser(Executor executor) { //TODO validation
        User user = userRepository.findByUsername(executor.getUsername()).get();
        user.setExecutorId(executor.getId());
        executor.setUserId(user.getId());
        Role role = addExecutorToRole(executor);
        user.addRole(role);
        userRepository.save(user);
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

