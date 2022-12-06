package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.assemblers.BookModelAssembler;
import com.ataatasoy.readingisgood.controllers.BookController;
import com.ataatasoy.readingisgood.exceptions.BookAlreadyExistsException;
import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;
import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.repository.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private Validator validator;
    @Autowired
    private BookRepository repository;
    @Autowired
    private BookModelAssembler assembler;

    public ResponseEntity<CollectionModel<EntityModel<Book>>> getAllBooks() {
        List<EntityModel<Book>> books = repository.findAll().stream() //
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Book>> booksModel = CollectionModel.of(books,
                linkTo(methodOn(BookController.class).all()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(booksModel);
    }

    public ResponseEntity<EntityModel<Book>> addNewBook(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Book> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
        try {
            EntityModel<Book> entityModel = assembler.toModel(repository.save(book));

            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (DataIntegrityViolationException e) {
            throw new BookAlreadyExistsException(book.getName());
        }
    }

    public ResponseEntity<EntityModel<Book>> getBook(Long id) {
        Book book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        EntityModel<Book> model = assembler.toModel(book);

        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    public ResponseEntity<EntityModel<Book>> updateBook(Long id, Book newBook) {
        Book updatedBook = repository.findById(id).map(book -> {
            // There has to be a better way
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

        EntityModel<Book> entityModel = assembler.toModel(updatedBook);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }
}
