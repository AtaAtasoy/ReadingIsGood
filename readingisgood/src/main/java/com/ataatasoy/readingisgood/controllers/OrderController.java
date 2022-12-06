package com.ataatasoy.readingisgood.controllers;

import com.ataatasoy.readingisgood.exceptions.*;
import com.ataatasoy.readingisgood.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;

import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.models.OrderStatus;
import com.ataatasoy.readingisgood.repository.OrderRepository;


@RestController
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderModelAssembler orderModelAssembler;
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> all() {
        List<EntityModel<Order>> orders = orderService.getAllOrders().stream()
                .map(orderModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Order>> orderModels = CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(orderModels);
    }

    @GetMapping("/orders/between")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> between(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<EntityModel<Order>> orders = orderService.getOrdersBetweenDate(start, end)
                .stream()
                .map(orderModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel()));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<EntityModel<Order>> one(@PathVariable Long id) {
        Order order = orderService.getOrder(id);

        EntityModel<Order> model = orderModelAssembler.toModel(order);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order newOrder) {
        Order savedOrder = orderService.addNewOrder(newOrder);
        EntityModel<Order> entityModel = orderModelAssembler.toModel(savedOrder);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = orderService.getOrder(id);

        if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            return ResponseEntity.ok(orderModelAssembler.toModel(orderRepository.save(order)));
        } else {
            throw new IllegalOrderMethodException(order.getOrderStatus());
        }
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<EntityModel<Order>> complete(@PathVariable Long id) {
        Order order = orderService.getOrder(id);

        if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            return ResponseEntity.ok(orderModelAssembler.toModel(orderRepository.save(order)));
        } else{
            throw new IllegalOrderMethodException(order.getOrderStatus());
        }
    }
}
