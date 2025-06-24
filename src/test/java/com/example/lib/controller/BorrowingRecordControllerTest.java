package com.example.lib.controller;

import com.example.lib.model.Book;
import com.example.lib.model.BorrowingRecord;
import com.example.lib.model.Customer;
import com.example.lib.service.BorrowingRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowingRecordController.class)
class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    private Book book;
    private Customer customer;
    private BorrowingRecord record;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Refactoring");

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Bob");
        customer.setContactInfo(9876543210L);

        record = new BorrowingRecord();
        record.setId(1L);
        record.setBook(book);
        record.setCustomer(customer);
        record.setBorrowDate(LocalDate.now());
    }

    @Test
    void testBorrowBook() throws Exception {
        when(borrowingRecordService.borrowBook(1L, 1L)).thenReturn(record);

        mockMvc.perform(post("/api/borrow/1/customer/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.book.title").value("Refactoring"))
                .andExpect(jsonPath("$.customer.name").value("Bob"));
    }

    @Test
    void testReturnBook() throws Exception {
        when(borrowingRecordService.returnBook(1L, 1L)).thenReturn(record);

        mockMvc.perform(put("/api/return/1/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.book.title").value("Refactoring"))
                .andExpect(jsonPath("$.customer.name").value("Bob"));
    }
}
