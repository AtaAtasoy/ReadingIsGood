package com.ataatasoy.readingisgood.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ataatasoy.readingisgood.controllers.OrderController;
import com.ataatasoy.readingisgood.models.Order;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

  @Override
  public EntityModel<Order> toModel(Order order) {

    return EntityModel.of(order, //
        linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
        linkTo(methodOn(OrderController.class).all()).withRel("orders"));
  }
}