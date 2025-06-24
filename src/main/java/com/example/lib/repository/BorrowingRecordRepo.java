package com.example.lib.repository;

import com.example.lib.model.Book;
import com.example.lib.model.BorrowingRecord;
import com.example.lib.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepo extends JpaRepository<BorrowingRecord, Long> {

    @Query("SELECT r FROM BorrowingRecord r WHERE r.book = :book AND r.customer = :customer AND r.returnDate IS NULL")
    Optional<BorrowingRecord> findByBookAndCustomer(@Param("book") Book book, @Param("customer") Customer customer);


    @Query("SELECT COUNT(b) FROM BorrowingRecord b WHERE b.customer.id = :id")
    Long countByCustomerId(@Param("id") Long id);



}
