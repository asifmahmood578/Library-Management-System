package com.example.lib.controller;

import com.example.lib.model.BorrowingRecord;
import com.example.lib.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingRecordController {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @PostMapping("/borrow/{bookId}/customer/{customerId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long bookId, @PathVariable Long customerId) {
        BorrowingRecord record = borrowingRecordService.borrowBook(bookId, customerId);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/customer/{customerId}")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long bookId, @PathVariable Long customerId) {
        BorrowingRecord record = borrowingRecordService.returnBook(bookId, customerId);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
}
