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
    public Executor registerExecutor(ExecutorRegisterDTO executorRegisterDTO, Long userId) throws ExecutorIsAlreadyRegisteredException, NoSuchUserException {

        if (!checkForUserRegistration(userId)) {
            throw new NoSuchUserException();
        }

        if (checkForExecutorRegistration(userId)) {
            throw new ExecutorIsAlreadyRegisteredException();
        }

        return saveExecutor(executorRegisterDTO,userId);

    }


    private boolean checkForCustomerRegistration(CustomerRegisterDTO dto) {
        return customerRepository.existsByUsernameAndEmail(dto.getUsername(), dto.getEmail());
    }

    private boolean checkForUserRegistration(Long userId) {
        return userRepository.existsById(userId);
    }

   private Customer saveCustomer(CustomerRegisterDTO dto) {
        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setEmail(dto.getEmail());
        customerRepository.save(customer);
        return customer;
    }


     private boolean checkForExecutorRegistration(Long userId) {
        User user = userRepository.findById(userId).get();
        return executorRepository.existsByUsernameAndEmail(user.getUsername(), user.getEmail());
    }

    private Executor saveExecutor(ExecutorRegisterDTO dto, Long userId)  {
        User user = userRepository.findById(userId).get();
        Executor executor = init(user);
        Role role = addExecutorToRole(executor);
        addExecutorToUser(executor,user,role);
        saveSpecializationsAndPrices(dto,executor);
        return executor;
    }

    private Executor init(User user) {
        Executor executor = new Executor();
        executor.setUsername(user.getUsername());
        executor.setEmail(user.getEmail());
        return executorRepository.save(executor);
    }

    private Role addExecutorToRole(Executor executor) {
        Role role = roleRepository.findByName(ERole.ROLE_EXECUTOR).get();
        role.addExecutor(executor);
        return roleRepository.save(role);
    }

    private void addExecutorToUser(Executor executor, User user, Role role) {
        user.setExecutorId(executor.getId());
        executor.setUserId(user.getId());
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

