package com.example.lib.service;

import com.example.lib.exception.*;
import com.example.lib.model.*;
import com.example.lib.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingRecordServiceTest {

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    @Mock
    private BorrowingRecordRepo borrowingRecordRepo;

    @Mock
    private BookRepo bookRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private FineRepo fineRepo;

    private Book book;
    private Customer customer;
    private BorrowingRecord record;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsBorrowed(false);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John");

        record = new BorrowingRecord();
        record.setId(1L);
        record.setBook(book);
        record.setCustomer(customer);
        record.setBorrowDate(LocalDate.now().minusDays(6));  // Simulate overdue
    }

    @Test
    void testBorrowBookSuccess() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.save(any())).thenReturn(record);

        BorrowingRecord result = borrowingRecordService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertTrue(book.getIsBorrowed());
        assertEquals("Test Book", result.getBook().getTitle());
    }

    @Test
    void testBorrowBookThrowsIfBookNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> borrowingRecordService.borrowBook(1L, 1L));
    }

    @Test
    void testBorrowBookThrowsIfCustomerNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> borrowingRecordService.borrowBook(1L, 1L));
    }

    @Test
    void testBorrowBookThrowsIfBookAlreadyBorrowed() {
        book.setIsBorrowed(true);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(BookAlreadyBorrowedException.class, () -> borrowingRecordService.borrowBook(1L, 1L));
    }

    @Test
    void testReturnBookWithFine() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findByBookAndCustomer(book, customer)).thenReturn(Optional.of(record));
        when(borrowingRecordRepo.save(any())).thenReturn(record);

        BorrowingRecord returned = borrowingRecordService.returnBook(1L, 1L);

        assertEquals(LocalDate.now(), returned.getReturnDate());
        verify(fineRepo, times(1)).save(any(Fine.class));  // Fine should be issued
        assertFalse(book.getIsBorrowed());
    }

    @Test
    void testReturnBookNoFine() {
        record.setBorrowDate(LocalDate.now().minusDays(3));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findByBookAndCustomer(book, customer)).thenReturn(Optional.of(record));
        when(borrowingRecordRepo.save(any())).thenReturn(record);

        BorrowingRecord returned = borrowingRecordService.returnBook(1L, 1L);

        verify(fineRepo, never()).save(any());
    }

    @Test
    void testReturnBookThrowsIfBookNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> borrowingRecordService.returnBook(1L, 1L));
    }

    @Test
    void testReturnBookThrowsIfCustomerNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> borrowingRecordService.returnBook(1L, 1L));
    }

    @Test
    void testReturnBookThrowsIfRecordNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findByBookAndCustomer(book, customer)).thenReturn(Optional.empty());

        assertThrows(BorrowingRecordNotFoundException.class, () -> borrowingRecordService.returnBook(1L, 1L));
    }
}
