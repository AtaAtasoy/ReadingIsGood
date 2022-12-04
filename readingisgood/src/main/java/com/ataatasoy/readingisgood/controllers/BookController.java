package com.ataatasoy.readingisgood.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.ataatasoy.readingisgood.assemblers.BookModelAssembler;
import com.ataatasoy.readingisgood.exceptions.BookAlreadyExistsException;
import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;
import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.repository.BookRepository;

import lombok.Data;

@Data
@RestController
public class BookController {

    private final BookRepository repository;
    private final BookModelAssembler assembler;

    @GetMapping("/books")
    public CollectionModel<EntityModel<Book>> all() {
        List<EntityModel<Book>> customers = repository.findAll().stream() //
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(customers, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    @PostMapping("/books")
    ResponseEntity<?> newCustomer(@RequestBody Book newBook) {
        try {
            EntityModel<Book> entityModel = assembler.toModel(repository.save(newBook));

            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (DataIntegrityViolationException e) {
            throw new BookAlreadyExistsException(newBook.getName());
        }
    }

    @GetMapping("/books/{id}")
    public EntityModel<Book> one(@PathVariable Long id) {
        Book book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        return assembler.toModel(book);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        Book updatedBook = repository.findById(id).map(book -> {
            book.setStock(newBook.getStock());
            book.setAuthor(newBook.getAuthor());
            book.setPrice(newBook.getPrice());
            book.setName(newBook.getName());
            return repository.save(book);
        })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });

        EntityModel<Book> entityModel = assembler.toModel(updatedBook);
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }
}
