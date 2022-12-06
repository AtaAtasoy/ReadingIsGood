package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;
import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.repository.BookRepository;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.statements.SpringRepeat;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
@SpringBootTest(classes = {Validator.class, BookService.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class BookServiceTest {

    @Autowired
    BookService service;

    @Mock
    BookRepository repository;

    @Test
    void getAllBooks() {
        List<Book> list = new ArrayList<>();
        Book b1 = new Book("1984", "George Orwell", 0, 19.84);
        Book b2 = new Book("Anna Karenina", "Leo Tolstoy", 10, 84.99);

        list.add(b1);
        list.add(b2);

        when(repository.findAll()).thenReturn(list);

        List<Book> bookList = service.getAllBooks();
        assertEquals(2, bookList.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void addNewBook() {
        Book book = new Book("Anna Karenina", "Leo Tolstoy", 10, 84.99);

        service.addNewBook(book);
        verify(repository, times(1)).save(book);
    }

    @Test
    void getBook() throws BookNotFoundException {
        when(repository.findById(1L).orElseThrow(() -> new BookNotFoundException(1L)))
                .thenReturn(new Book("Anna Karenina","Leo Tolstoy", 10,84.99));

        Book book = service.getBook(1L);
        assertEquals("Anna Karenina", book.getName());
        assertEquals("Leo Tolstoy", book.getAuthor());
        assertEquals(10, book.getStock());
        assertEquals(84.99, book.getPrice());
    }

    @Test
    void updateBook() {
        Book book = new Book("Anna Karenina", "Leo Tolstoy", 10, 84.99);
        book.setId(1L);

        given(repository.save(book)).willReturn(book);
        book.setName("War and Peace");
        book.setPrice(59.99);
        final Book expected = service.updateBook(1L, book);

        assertNotNull(expected);
        assertEquals("War and Peace", expected.getName());
        assertEquals(10, expected.getStock());
        assertEquals(59.99, expected.getPrice());
        assertEquals("Leo Tolstoy", expected.getAuthor());
        verify(repository).save(any(Book.class));
    }
}