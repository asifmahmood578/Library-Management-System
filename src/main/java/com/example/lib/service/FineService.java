package com.example.lib.service;

import com.example.lib.exception.CustomerNotFoundException;
import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    private static final Logger logger = LoggerFactory.getLogger(FineService.class);

    @Autowired
    private FineRepo fineRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public List<Fine> getFinesByCustomerId(Long customerId) {
        logger.info("Fetching fines for customer ID: {}", customerId);

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer with ID {} not found", customerId);
                    return new CustomerNotFoundException("Customer not found with ID: " + customerId);
                });

        List<Fine> fines = fineRepo.findByBorrowingRecord_Customer(customer);
        logger.debug("Total fines found for customer ID {}: {}", customerId, fines.size());

        return fines;
    }
}
