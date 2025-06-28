package com.example.lib.controller;

import com.example.lib.model.Book;
import com.example.lib.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setPublicationYear(2008);
        book.setIsBorrowed(false);
    }

    @Test
    void testGetBooks() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void testGetBookById_Found() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        when(bookService.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddBook() throws Exception {
        when(bookService.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Clean Code\",\"author\":\"Robert C. Martin\",\"publicationYear\":2008}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void testEditBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(bookService.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Clean Code\",\"author\":\"Robert C. Martin\",\"publicationYear\":2008}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void testDeleteBook() throws Exception {
        when(bookService.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBook_NotFound() throws Exception {
        when(bookService.existsById(2L)).thenReturn(false);

        mockMvc.perform(delete("/api/books/delete/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchBooks() throws Exception {
        when(bookService.searchBooksByTitle("Clean")).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "Clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }
}
