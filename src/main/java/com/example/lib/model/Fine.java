package com.example.lib.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fine")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @OneToOne
    @JoinColumn(name = "borrowing_record_id")
    private BorrowingRecord borrowingRecord;

    // ----- Getters and Setters -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public BorrowingRecord getBorrowingRecord() {
        return borrowingRecord;
    }

    public void setBorrowingRecord(BorrowingRecord borrowingRecord) {
        this.borrowingRecord = borrowingRecord;
    }
}
