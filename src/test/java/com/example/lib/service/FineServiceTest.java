package com.example.lib.service;

import com.example.lib.exception.CustomerNotFoundException;
import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FineServiceTest {

    @InjectMocks
    private FineService fineService;

    @Mock
    private FineRepo fineRepo;

    @Mock
    private CustomerRepo customerRepo;

    private Customer customer;
    private Fine fine1, fine2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setContactInfo(9999999999L);

        fine1 = new Fine();
        fine1.setId(1L);
        fine1.setAmount(50.0);

        fine2 = new Fine();
        fine2.setId(2L);
        fine2.setAmount(100.0);
    }

    @Test
    void testGetFinesByCustomerIdReturnsFines() {
        Long customerId = 1L;
        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(fineRepo.findByBorrowingRecord_Customer(customer)).thenReturn(List.of(fine1, fine2));

        List<Fine> result = fineService.getFinesByCustomerId(customerId);

        assertEquals(2, result.size());
        assertEquals(50.0, result.get(0).getAmount());
        assertEquals(100.0, result.get(1).getAmount());
    }

    @Test
    void testGetFinesByCustomerIdReturnsEmptyList() {
        Long customerId = 2L;
        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(fineRepo.findByBorrowingRecord_Customer(customer)).thenReturn(Collections.emptyList());

        List<Fine> result = fineService.getFinesByCustomerId(customerId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFinesByCustomerIdThrowsIfCustomerNotFound() {
        Long invalidCustomerId = 99L;
        when(customerRepo.findById(invalidCustomerId)).thenReturn(Optional.empty());

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class, () -> {
            fineService.getFinesByCustomerId(invalidCustomerId);
        });

        assertEquals("Customer not found with ID: 99", ex.getMessage());
    }
}
