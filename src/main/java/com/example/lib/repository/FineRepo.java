package com.example.lib.repository;

import com.example.lib.model.Customer;
import com.example.lib.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepo extends JpaRepository<Fine, Long> {

    // ✅ This works because Fine -> BorrowingRecord -> Customer
    List<Fine> findByBorrowingRecord_Customer(Customer customer);


}
