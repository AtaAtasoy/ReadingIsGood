package com.ataatasoy.readingisgood.controllers;

import java.util.List;

import com.ataatasoy.readingisgood.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.ataatasoy.readingisgood.assemblers.BookModelAssembler;
import com.ataatasoy.readingisgood.exceptions.InvalidBookException;
import com.ataatasoy.readingisgood.models.Book;

@RestController
public class BookController {
    @Autowired
    private BookService service;

    @Autowired BookModelAssembler assembler;

    @GetMapping("/books")
    public ResponseEntity<CollectionModel<EntityModel<Book>>> all() {
        List<Book> books = service.getAllBooks();

        List<EntityModel<Book>> bookModels = books.stream()
                .map(assembler::toModel)
                .toList();

        CollectionModel<EntityModel<Book>> booksModel = CollectionModel.of(bookModels,
                linkTo(methodOn(BookController.class).all()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(booksModel);
    }

    @PostMapping("/books")
    ResponseEntity<EntityModel<Book>> newBook(@RequestBody Book newBook) {
        Book book = service.addNewBook(newBook);
        try {
            EntityModel<Book> entityModel = assembler.toModel(book);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (IllegalArgumentException e) {
            throw new InvalidBookException(e.getMessage());
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<EntityModel<Book>> one(@PathVariable Long id) {
        Book book = service.getBook(id);
        EntityModel<Book> model = assembler.toModel(book);

        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        Book updatedBook = service.updateBook(id, newBook);
        EntityModel<Book> entityModel = assembler.toModel(updatedBook);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }
}
