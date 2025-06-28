package com.example.lib.controller;

import com.example.lib.model.Customer;
import com.example.lib.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Customer customer = customerService.findByIdOrThrow(id); // updated
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody @Valid Customer customer) {
        Customer saved = customerService.save(customer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Customer> editCustomer(@PathVariable Long id, @RequestBody @Valid Customer updated) {
        Customer existing = customerService.findByIdOrThrow(id); // updated
        existing.setName(updated.getName());
        existing.setContactInfo(updated.getContactInfo());
        Customer saved = customerService.save(existing);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.deleteById(id); // will throw if not found
        return ResponseEntity.ok().build();
    }
}
