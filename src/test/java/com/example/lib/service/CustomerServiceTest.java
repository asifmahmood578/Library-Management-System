package com.example.lib.service;

import com.example.lib.exception.CustomerNotFoundException;
import com.example.lib.model.Customer;
import com.example.lib.repository.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
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
    void testFindAll() {
        when(customerRepo.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.findAll();
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void testFindByIdOrThrow_Found() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findByIdOrThrow(1L);
        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void testFindByIdOrThrow_NotFound() {
        when(customerRepo.findById(2L)).thenReturn(Optional.empty());

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class,
                () -> customerService.findByIdOrThrow(2L));

        assertEquals("Customer with ID 2 not found", ex.getMessage());
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
    void testDeleteByIdSuccess() {
        when(customerRepo.existsById(1L)).thenReturn(true);

        customerService.deleteById(1L);
        verify(customerRepo).deleteById(1L);
    }

    @Test
    void testDeleteByIdThrowsIfNotFound() {
        when(customerRepo.existsById(99L)).thenReturn(false);

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class,
                () -> customerService.deleteById(99L));

        assertEquals("Customer with ID 99 not found", ex.getMessage());
    }
}
