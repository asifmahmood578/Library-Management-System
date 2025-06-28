package com.example.lib.service;

import com.example.lib.exception.BookAlreadyBorrowedException;
import com.example.lib.exception.BookNotFoundException;
import com.example.lib.exception.BorrowingRecordNotFoundException;
import com.example.lib.exception.CustomerNotFoundException;
import com.example.lib.model.Book;
import com.example.lib.model.BorrowingRecord;
import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.BookRepo;
import com.example.lib.repository.BorrowingRecordRepo;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BorrowingRecordService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowingRecordService.class);

    @Autowired
    private BorrowingRecordRepo borrowingRecordRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private FineRepo fineRepo;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long customerId) {
        logger.info("Borrowing book with ID {} for customer ID {}", bookId, customerId);

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found: {}", bookId);
                    return new BookNotFoundException("Book not found with ID: " + bookId);
                });

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer not found: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with ID: " + customerId);
                });

        if (book.getIsBorrowed()) {
            logger.warn("Book already borrowed: {}", bookId);
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        book.setIsBorrowed(true);

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setCustomer(customer);
        record.setBorrowDate(LocalDate.now());

        logger.info("Book borrowed successfully");
        return borrowingRecordRepo.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long customerId) {
        logger.info("Returning book ID {} for customer ID {}", bookId, customerId);

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found: {}", bookId);
                    return new BookNotFoundException("Book not found with ID: " + bookId);
                });

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer not found: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with ID: " + customerId);
                });

        BorrowingRecord record = borrowingRecordRepo.findByBookAndCustomer(book, customer)
                .orElseThrow(() -> {
                    logger.error("Borrowing record not found for book ID {} and customer ID {}", bookId, customerId);
                    return new BorrowingRecordNotFoundException("Borrowing record not found");
                });

        record.setReturnDate(LocalDate.now());
        book.setIsBorrowed(false);

        long daysBorrowed = ChronoUnit.DAYS.between(record.getBorrowDate(), record.getReturnDate());
        logger.info("Book was borrowed for {} days", daysBorrowed);

        if (daysBorrowed > 5) {
            double fineAmount = (daysBorrowed - 5) * 5.0;
            Fine fine = new Fine();
            fine.setAmount(fineAmount);
            fine.setIssuedDate(LocalDate.now());
            fine.setBorrowingRecord(record);
            fineRepo.save(fine);
            logger.info("Fine of ₹{} issued for book ID {}", fineAmount, bookId);
        }

        logger.info("Book returned successfully");
        return borrowingRecordRepo.save(record);
    }
}
