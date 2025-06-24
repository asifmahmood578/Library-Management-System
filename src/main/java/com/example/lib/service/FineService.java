package com.example.lib.service;

import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepo fineRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public List<Fine> getFinesByCustomerId(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return fineRepo.findByBorrowingRecord_Customer(customer);
    }
}
