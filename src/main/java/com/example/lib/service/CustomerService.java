package com.example.lib.service;

import com.example.lib.model.Customer;
import com.example.lib.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepo.findById(id);
    }

    @Transactional
    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    public Boolean existsById(Long id) {
        return customerRepo.existsById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        customerRepo.deleteById(id);
    }
}
