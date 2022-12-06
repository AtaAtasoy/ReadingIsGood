package com.ataatasoy.readingisgood.controllers;

import com.ataatasoy.readingisgood.exceptions.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;

import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.models.OrderDetail;
import com.ataatasoy.readingisgood.models.OrderStatus;
import com.ataatasoy.readingisgood.repository.BookRepository;
import com.ataatasoy.readingisgood.repository.CustomerRepository;
import com.ataatasoy.readingisgood.repository.OrderDetailRepository;
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
    private final OrderDetailRepository orderDetailRepository;

    @GetMapping("/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> all() {
        List<EntityModel<Order>> orders = orderRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (orders.size() == 0){
            throw new OrdersDoNotExistException();
        } else {
            CollectionModel<EntityModel<Order>> orderModels = CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
            return ResponseEntity.status(HttpStatus.OK).body(orderModels);
        }
    }

    @GetMapping("/orders/between")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> between(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<EntityModel<Order>> orders = orderRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (orders.size() == 0){
            throw new OrderNotFoundInRangeException(start, end);
        } else {
            return ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel()));
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<EntityModel<Order>> one(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        EntityModel<Order> model = assembler.toModel(order);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order newOrder) {
        newOrder.setOrderStatus(OrderStatus.IN_PROGRESS);
        List<Book> parsedBooks = new ArrayList<>();

        // Update stock
        for (Book bookInOrder : newOrder.getOrderedBooks()) {
            Book savedBook = bookRepository.findById(bookInOrder.getId())
                    .orElseThrow(() -> new BookNotFoundException(bookInOrder.getId()));

            int quantity = bookInOrder.getQuantity();
            OrderDetail oq = new OrderDetail();
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
        newOrder.getCustomer().addOrder(newOrder);

        EntityModel<Order> entityModel = assembler.toModel(orderRepository.save(newOrder));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        } else {
            throw new IllegalOrderMethodException(order.getOrderStatus());
        }
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {

        Order order = orderRepository.findById(id) //
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        } else{
            throw new IllegalOrderMethodException(order.getOrderStatus());
        }
    }
}
