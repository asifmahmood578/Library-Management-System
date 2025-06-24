package com.example.lib.service;

import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.model.Book;
import com.example.lib.model.BorrowingRecord;
import com.example.lib.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private BookRepo bookRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private FineRepo fineRepo;

    @Mock
    private BorrowingRecordRepo borrowingRecordRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdminDashboard() {
        when(bookRepo.count()).thenReturn(100L);
        when(customerRepo.count()).thenReturn(40L);

        Book b1 = new Book();
        b1.setIsBorrowed(true);
        Book b2 = new Book();
        b2.setIsBorrowed(true);
        Book b3 = new Book();
        b3.setIsBorrowed(false);
        when(bookRepo.findAll()).thenReturn(List.of(b1, b2, b3)); // 2 borrowed books

        Fine fine1 = new Fine();
        fine1.setAmount(50.0);
        Fine fine2 = new Fine();
        fine2.setAmount(100.0);
        when(fineRepo.findAll()).thenReturn(List.of(fine1, fine2));

        Map<String, Object> result = dashboardService.getAdminDashboard();

        assertEquals(100L, result.get("totalBooks"));
        assertEquals(40L, result.get("totalCustomers"));
        assertEquals(2L, result.get("totalBorrowedBooks"));
        assertEquals(150.0, result.get("totalFinesCollected"));
    }

    @Test
    void testGetCustomerDashboard() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John");

        Book book = new Book();
        book.setTitle("Test Book");

        BorrowingRecord br1 = new BorrowingRecord();
        br1.setCustomer(customer);
        br1.setBook(book);
        br1.setReturnDate(null); // still borrowed

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findAll()).thenReturn(List.of(br1));

        Fine fine = new Fine();
        fine.setAmount(50.0);
        when(fineRepo.findByBorrowingRecord_Customer(customer)).thenReturn(List.of(fine));

        Map<String, Object> result = dashboardService.getCustomerDashboard(customerId);

        assertEquals("John", result.get("customerName"));
        assertEquals(List.of("Test Book"), result.get("borrowedBooks"));
        assertEquals(50.0, result.get("totalFines"));
    }
}
