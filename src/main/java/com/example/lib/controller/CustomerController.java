package com.example.lib.controller;

import com.example.lib.model.Customer;
import com.example.lib.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody @Valid Customer customer) {
        Customer saved = customerService.save(customer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Customer> editCustomer(@PathVariable Long id, @RequestBody @Valid Customer updated) {
        return customerService.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setContactInfo(updated.getContactInfo());
            return new ResponseEntity<>(customerService.save(existing), HttpStatus.OK);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        boolean exists = customerService.existsById(id);
        if (exists) {
            customerService.deleteById(id);
        }
        return new ResponseEntity<>(exists, exists ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
