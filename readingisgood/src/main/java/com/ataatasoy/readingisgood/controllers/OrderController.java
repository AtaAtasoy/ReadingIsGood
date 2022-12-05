package com.ataatasoy.readingisgood.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ataatasoy.readingisgood.assemblers.OrderModelAssembler;
import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;
import com.ataatasoy.readingisgood.exceptions.CustomerNotFoundException;
import com.ataatasoy.readingisgood.exceptions.OrderNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;

import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.models.OrderQuantity;
import com.ataatasoy.readingisgood.models.Status;
import com.ataatasoy.readingisgood.repository.BookRepository;
import com.ataatasoy.readingisgood.repository.CustomerRepository;
import com.ataatasoy.readingisgood.repository.OrderQuantityRepository;
import com.ataatasoy.readingisgood.repository.OrderRepository;

import lombok.Data;

@Data
@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderModelAssembler assembler;
    private final CustomerController customerController;
    private final BookController bookController;
    private final CustomerRepository customerRepository;
    private final OrderQuantityRepository orderQuantityRepository;

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = orderRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/between")
    public CollectionModel<EntityModel<Order>> between(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<EntityModel<Order>> orders = orderRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<?> newOrder(@RequestBody Order newOrder) {
        newOrder.setStatus(Status.IN_PROGRESS);
        List<Long> bookIdList = new ArrayList<>();
        List<Book> parsedBooks = new ArrayList<>();
        
        for (Book bookInOrder : newOrder.getOrderedBooks()) {
            bookIdList.add(bookInOrder.getId());
        }

        try {
            // Update stock
            for (Book bookInOrder : newOrder.getOrderedBooks()) {
                Book savedBook = bookRepository.findById(bookInOrder.getId())
                        .orElseThrow(() -> new BookNotFoundException(bookInOrder.getId()));
                int quantity = bookInOrder.getQuantity();
                OrderQuantity oq = new OrderQuantity();
                oq.setBook(savedBook);
                oq.setOrder(newOrder);
                oq.setQuantity(quantity);
                oq.setPrice(bookInOrder.getPrice());

                newOrder.addQuantity(oq);
                
                savedBook.setStock(savedBook.getStock() - quantity);
                savedBook.addQuantity(oq);

                parsedBooks.add(savedBook);
            }

            // Populate the non-given fields using the records in the DB
            newOrder.setOrderedBooks(parsedBooks);
            Customer savedCustomer = customerRepository.findById(newOrder.getCustomer().getId())
                    .orElseThrow(() -> new CustomerNotFoundException(newOrder.getCustomer().getId()));

            newOrder.setCustomer(savedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Could not find books"));
        }
        newOrder.getCustomer().addOrder(newOrder);

        return ResponseEntity.ok(EntityModel.of(orderRepository.save(newOrder),
                linkTo(methodOn(OrderController.class).one(newOrder.getId())).withSelfRel()));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {

        Order order = orderRepository.findById(id) //
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
