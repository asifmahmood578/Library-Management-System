package com.example.lib.service;

import com.example.lib.model.Book;
import com.example.lib.model.BorrowingRecord;
import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.BookRepo;
import com.example.lib.repository.BorrowingRecordRepo;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BorrowingRecordService {

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
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (book.getIsBorrowed()) {
            throw new RuntimeException("Book is already borrowed");
        }

        book.setIsBorrowed(true);

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setCustomer(customer);
        record.setBorrowDate(LocalDate.now());

        return borrowingRecordRepo.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long customerId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        BorrowingRecord record = borrowingRecordRepo.findByBookAndCustomer(book, customer)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        record.setReturnDate(LocalDate.now());
        book.setIsBorrowed(false);

        long daysBorrowed = ChronoUnit.DAYS.between(record.getBorrowDate(), record.getReturnDate());

        if (daysBorrowed > 5) {
            double fineAmount = (daysBorrowed - 5) * 5.0;
            Fine fine = new Fine();
            fine.setAmount(fineAmount);
            fine.setIssuedDate(LocalDate.now());
            fine.setBorrowingRecord(record);
            fineRepo.save(fine);
        }

        return borrowingRecordRepo.save(record);
    }
}
