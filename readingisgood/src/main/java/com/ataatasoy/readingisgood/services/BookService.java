package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;
import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.repository.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookService {

    @Autowired
    private Validator validator;
    @Autowired
    private BookRepository repository;

    private void validateBook(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Book> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = repository.findAll();
        if (books.isEmpty()){
            throw new BookNotFoundException(0L);
        }
        return books;
    }

    public Book addNewBook(Book book) {
        validateBook(book);
        return repository.save(book);
    }

    public Book getBook(Long id) {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book updateBook(Long id, Book newBook) {
        return repository.findById(id).map(book -> {
            if (newBook.getAuthor() != null)
                book.setAuthor(newBook.getAuthor());

            if (newBook.getName() != null)
                book.setName(newBook.getName());

            if (newBook.getStock() != null)
                book.setStock(newBook.getStock());

            if (newBook.getPrice() != null)
                book.setPrice(newBook.getPrice());

            return repository.save(book);
        })
        .orElseGet(() -> {
            newBook.setId(id);
            return repository.save(newBook);
        });
    }
}
