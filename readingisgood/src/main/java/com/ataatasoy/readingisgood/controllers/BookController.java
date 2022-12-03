package com.ataatasoy.readingisgood.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.repository.BookRepository;

import lombok.Data;

@Data
@RestController
public class BookController {
    
    private final BookRepository repository;

    @PostMapping("/books")
    Book newBook(@RequestBody Book newBook){
        return repository.save(newBook);
    }

    @PutMapping("/books/{id}")
    Book updateBook(@RequestBody Book newBook, @PathVariable Long id){
        return newBook;
    }
}
