package com.ataatasoy.readingisgood.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ataatasoy.readingisgood.exceptions.OrderNotFoundException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;

import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.repository.OrderRepository;

@RestController
public class OrderController {
    private final OrderRepository repository;

    OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
                        linkTo(methodOn(OrderController.class).all()).withRel("orders")))
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> one(@PathVariable long id) {
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        return EntityModel.of(order, linkTo(methodOn(OrderController.class).one(id)).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders"));
    }
}
