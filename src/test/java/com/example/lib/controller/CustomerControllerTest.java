package com.example.lib.controller;

import com.example.lib.model.Customer;
import com.example.lib.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
        customer.setContactInfo(9876543210L);
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerService.findAll()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void testGetCustomerById_Found() throws Exception {
        when(customerService.findById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        when(customerService.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCustomer() throws Exception {
        when(customerService.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"contactInfo\":9876543210}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testEditCustomer() throws Exception {
        when(customerService.findById(1L)).thenReturn(Optional.of(customer));
        when(customerService.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(put("/api/customers/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"contactInfo\":9876543210}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        when(customerService.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCustomer_NotFound() throws Exception {
        when(customerService.existsById(2L)).thenReturn(false);

        mockMvc.perform(delete("/api/customers/delete/2"))
                .andExpect(status().isNotFound());
    }
}
