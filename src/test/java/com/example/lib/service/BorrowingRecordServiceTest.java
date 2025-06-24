package com.example.lib.service;

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
    void testReturnBookWithFine() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findByBookAndCustomer(book, customer)).thenReturn(Optional.of(record));
        when(borrowingRecordRepo.save(any())).thenReturn(record);

        BorrowingRecord returned = borrowingRecordService.returnBook(1L, 1L);

        assertEquals(LocalDate.now(), returned.getReturnDate());
        verify(fineRepo, times(1)).save(any(Fine.class));  // Fine should be issued
        assertFalse(book.getIsBorrowed()); // should be false after return
    }

    @Test
    void testReturnBookNoFine() {
        record.setBorrowDate(LocalDate.now().minusDays(3)); // not overdue
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findByBookAndCustomer(book, customer)).thenReturn(Optional.of(record));
        when(borrowingRecordRepo.save(any())).thenReturn(record);

        BorrowingRecord returned = borrowingRecordService.returnBook(1L, 1L);

        verify(fineRepo, never()).save(any()); // no fine
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
    void testBorrowBookThrowsIfBookAlreadyBorrowed() {
        book.setIsBorrowed(true);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> borrowingRecordService.borrowBook(1L, 1L));

        assertEquals("Book is already borrowed", ex.getMessage());

    }

    @Test
    void testReturnBookRecordNotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordRepo.findByBookAndCustomer(book, customer)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> borrowingRecordService.returnBook(1L, 1L));

        assertEquals("Borrowing record not found", ex.getMessage());
    }
}
