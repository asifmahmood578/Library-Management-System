package com.example.lib.service;

import com.example.lib.model.Book;
import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import com.example.lib.repository.BookRepo;
import com.example.lib.repository.BorrowingRecordRepo;
import com.example.lib.repository.CustomerRepo;
import com.example.lib.repository.FineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BorrowingRecordRepo borrowingRecordRepo;

    @Autowired
    private FineRepo fineRepo;

    public Map<String, Object> getAdminDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalBooks", bookRepo.count());
        response.put("totalCustomers", customerRepo.count());
        response.put("totalBorrowedBooks", bookRepo.findAll().stream().filter(Book::getIsBorrowed).count());
        response.put("totalFinesCollected", fineRepo.findAll().stream().mapToDouble(Fine::getAmount).sum());
        return response;
    }

    public Map<String, Object> getCustomerDashboard(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<String> borrowedBookTitles = borrowingRecordRepo.findAll().stream()
                .filter(r -> r.getCustomer().getId().equals(customerId) && r.getReturnDate() == null)
                .map(r -> r.getBook().getTitle())
                .collect(Collectors.toList());

        double totalFine = fineRepo.findByBorrowingRecord_Customer(customer).stream()
                .mapToDouble(Fine::getAmount)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("customerName", customer.getName());
        response.put("borrowedBooks", borrowedBookTitles);
        response.put("totalFines", totalFine);

        return response;
    }
}
