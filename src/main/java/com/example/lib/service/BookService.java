package com.example.lib.service;

import com.example.lib.exception.BookNotFoundException;
import com.example.lib.model.Book;
import com.example.lib.repository.BookRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepo bookRepo;

    public List<Book> findAll() {
        logger.info("Fetching all books");
        return bookRepo.findAll();
    }

    public Book findByIdOrThrow(Long id) {
        logger.info("Fetching book with ID: {}", id);
        return bookRepo.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    public Optional<Book> findById(Long id) {
        logger.info("Fetching book with ID: {}", id);
        return bookRepo.findById(id);
    }

    @Transactional
    public Book save(Book book) {
        logger.info("Saving book: {}", book.getTitle());
        return bookRepo.save(book);
    }

    public Boolean existsById(Long id) {
        boolean exists = bookRepo.existsById(id);
        logger.info("Checking if book exists with ID {}: {}", id, exists);
        return exists;
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting book with ID: {}", id);
        if (!bookRepo.existsById(id)) {
            throw new BookNotFoundException("Cannot delete. Book not found with ID: " + id);
        }
        bookRepo.deleteById(id);
    }

    public List<Book> searchBooksByTitle(String title) {
        logger.info("Searching books with title containing: {}", title);
        return bookRepo.findAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
}
