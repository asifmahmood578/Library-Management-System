package com.example.lib.controller;

import com.example.lib.model.Book;
import com.example.lib.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    private final ObjectMapper objectMapper;

    public BookController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<> (books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        Optional<Book> book = bookService.findById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody @Valid Book book) {
        Book newBook = bookService.save(book);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Book> editBook(@RequestBody Book editedBook, @PathVariable Long id) {
        return bookService.findById(id).map(existingBook -> {
            existingBook.setAuthor(editedBook.getAuthor());
            existingBook.setTitle(editedBook.getTitle());
            existingBook.setPublicationYear(editedBook.getPublicationYear());

            Book updatedBook = bookService.save(existingBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
        Boolean isValid = bookService.existsById(id);
        if (isValid)
            bookService.deleteById(id);
        return new ResponseEntity<>(isValid, isValid ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


}
