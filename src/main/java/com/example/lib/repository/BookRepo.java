package com.example.lib.repository;

import com.example.lib.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.isBorrowed = :status")
    Long countByIsBorrowed(@Param("status") Boolean status);





}
