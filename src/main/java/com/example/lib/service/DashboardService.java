package com.example.lib.service;

import com.example.lib.exception.CustomerNotFoundException;
import com.example.lib.model.Book;
import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.BookRepo;
import com.example.lib.repository.BorrowingRecordRepo;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BorrowingRecordRepo borrowingRecordRepo;

    @Autowired
    private FineRepo fineRepo;

    public Map<String, Object> getAdminDashboard() {
        logger.info("Generating admin dashboard summary...");

        long totalBooks = bookRepo.count();
        long totalCustomers = customerRepo.count();
        long totalBorrowedBooks = bookRepo.findAll().stream().filter(Book::getIsBorrowed).count();
        double totalFines = fineRepo.findAll().stream().mapToDouble(Fine::getAmount).sum();

        logger.debug("Total books: {}", totalBooks);
        logger.debug("Total customers: {}", totalCustomers);
        logger.debug("Total borrowed books: {}", totalBorrowedBooks);
        logger.debug("Total fines collected: {}", totalFines);

        Map<String, Object> response = new HashMap<>();
        response.put("totalBooks", totalBooks);
        response.put("totalCustomers", totalCustomers);
        response.put("totalBorrowedBooks", totalBorrowedBooks);
        response.put("totalFinesCollected", totalFines);

        return response;
    }

    public Map<String, Object> getCustomerDashboard(Long customerId) {
        logger.info("Generating dashboard for customer ID: {}", customerId);

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer with ID {} not found", customerId);
                    return new CustomerNotFoundException("Customer not found with ID: " + customerId);
                });

        List<String> borrowedBookTitles = borrowingRecordRepo.findAll().stream()
                .filter(r -> r.getCustomer().getId().equals(customerId) && r.getReturnDate() == null)
                .map(r -> r.getBook().getTitle())
                .collect(Collectors.toList());

        double totalFine = fineRepo.findByBorrowingRecord_Customer(customer).stream()
                .mapToDouble(Fine::getAmount)
                .sum();

        logger.debug("Customer name: {}", customer.getName());
        logger.debug("Borrowed books: {}", borrowedBookTitles);
        logger.debug("Total fine: {}", totalFine);

        Map<String, Object> response = new HashMap<>();
        response.put("customerName", customer.getName());
        response.put("borrowedBooks", borrowedBookTitles);
        response.put("totalFines", totalFine);

        return response;
    }
}
