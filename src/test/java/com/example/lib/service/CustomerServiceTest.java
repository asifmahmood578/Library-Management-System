package com.example.lib.service;

import com.example.lib.model.Customer;
import com.example.lib.repository.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
        customer.setContactInfo(9876543210L);
    }

    @Test
    void testFindAll() {
        List<Customer> mockList = List.of(customer);
        when(customerRepo.findAll()).thenReturn(mockList);

        List<Customer> result = customerService.findAll();
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void testFindById_Found() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        when(customerRepo.findById(2L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.findById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        Customer saved = customerService.save(customer);
        assertNotNull(saved);
        assertEquals("Alice", saved.getName());
    }

    @Test
    void testExistsById() {
        when(customerRepo.existsById(1L)).thenReturn(true);
        assertTrue(customerService.existsById(1L));

        when(customerRepo.existsById(2L)).thenReturn(false);
        assertFalse(customerService.existsById(2L));
    }

    @Test
    void testDeleteById() {
        customerService.deleteById(1L);
        verify(customerRepo, times(1)).deleteById(1L);
    }
}
