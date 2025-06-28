package com.example.lib.service;

import com.example.lib.exception.BookNotFoundException;
import com.example.lib.model.Book;
import com.example.lib.repository.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPublicationYear(2018);
        book.setIsBorrowed(false);
    }

    @Test
    void testFindAll() {
        when(bookRepo.findAll()).thenReturn(List.of(book));

        List<Book> books = bookService.findAll();

        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
    }

    @Test
    void testFindById_Found() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Effective Java", result.get().getTitle());
    }

    @Test
    void testFindById_NotFound() {
        when(bookRepo.findById(2L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByIdOrThrow_Success() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.findByIdOrThrow(1L);

        assertNotNull(result);
        assertEquals("Effective Java", result.getTitle());
    }

    @Test
    void testFindByIdOrThrow_ThrowsException() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.findByIdOrThrow(1L));

        assertEquals("Book not found with ID: 1", ex.getMessage());
    }

    @Test
    void testSave() {
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book saved = bookService.save(book);

        assertNotNull(saved);
        assertEquals("Effective Java", saved.getTitle());
    }

    @Test
    void testExistsById() {
        when(bookRepo.existsById(1L)).thenReturn(true);
        assertTrue(bookService.existsById(1L));

        when(bookRepo.existsById(2L)).thenReturn(false);
        assertFalse(bookService.existsById(2L));
    }

    @Test
    void testDeleteByIdSuccess() {
        when(bookRepo.existsById(1L)).thenReturn(true);

        bookService.deleteById(1L);

        verify(bookRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdThrowsIfBookNotFound() {
        when(bookRepo.existsById(1L)).thenReturn(false);

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> bookService.deleteById(1L));

        assertEquals("Cannot delete. Book not found with ID: 1", ex.getMessage());
    }

    @Test
    void testSearchBooksByTitle() {
        Book anotherBook = new Book();
        anotherBook.setId(2L);
        anotherBook.setTitle("Java Concurrency");
        anotherBook.setAuthor("Author B");
        anotherBook.setPublicationYear(2019);
        anotherBook.setIsBorrowed(false);

        when(bookRepo.findAll()).thenReturn(List.of(book, anotherBook));

        List<Book> result = bookService.searchBooksByTitle("Java");

        assertEquals(2, result.size());
        assertTrue(result.get(0).getTitle().contains("Java"));
        assertTrue(result.get(1).getTitle().contains("Java"));
    }
}
