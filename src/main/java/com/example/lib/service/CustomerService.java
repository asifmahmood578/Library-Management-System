package com.example.lib.service;

import com.example.lib.exception.CustomerNotFoundException;
import com.example.lib.model.Customer;
import com.example.lib.repository.CustomerRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepo customerRepo;

    public List<Customer> findAll() {
        logger.info("Fetching all customers");
        return customerRepo.findAll();
    }

    public Customer findByIdOrThrow(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
    }


    @Transactional
    public Customer save(Customer customer) {
        logger.info("Saving customer: {}", customer.getName());
        return customerRepo.save(customer);
    }

    public Boolean existsById(Long id) {
        boolean exists = customerRepo.existsById(id);
        logger.info("Customer exists with ID {}: {}", id, exists);
        return exists;
    }

    @Transactional
    public void deleteById(Long id) {
        if (!customerRepo.existsById(id)) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        customerRepo.deleteById(id);
    }

}
