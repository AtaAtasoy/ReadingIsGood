package com.ataatasoy.readingisgood.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ataatasoy.readingisgood.controllers.BookController;
import com.ataatasoy.readingisgood.controllers.CustomerController;
import com.ataatasoy.readingisgood.models.Book;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {

  @Override
  public EntityModel<Book> toModel(Book book) {

    return EntityModel.of(book, //
        linkTo(methodOn(BookController.class).one(book.getId())).withSelfRel(),
        linkTo(methodOn(BookController.class).all()).withRel("customers"));
  }
}