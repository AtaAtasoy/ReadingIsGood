package com.ataatasoy.readingisgood.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ataatasoy.readingisgood.assemblers.OrderModelAssembler;
import com.ataatasoy.readingisgood.exceptions.OrderNotFoundException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;

import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.repository.OrderRepository;

import lombok.Data;

@Data
@RestController
public class OrderController {
    private final OrderRepository repository;
    private final OrderModelAssembler assembler;
    
    @GetMapping("/orders")
    public
    CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public
    EntityModel<Order> one(@PathVariable Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    EntityModel<Order> newOrder(@RequestBody Order newOrder){
        //TODO: Update the stocks
        return EntityModel.of(repository.save(newOrder), linkTo(methodOn(OrderController.class).one(newOrder.getId())).withSelfRel());
    }
}
