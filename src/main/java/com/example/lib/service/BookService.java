package com.example.lib.service;

import com.example.lib.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lib.repository.BookRepo;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepo.findById(id);
    }

    @Transactional
    public Book save(Book book) {
        return bookRepo.save(book);
    }

    public Boolean existsById(Long id) {
        return bookRepo.existsById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        bookRepo.deleteById(id);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepo.findByTitleContainingIgnoreCase(title);
    }
}

