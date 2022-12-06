package com.ataatasoy.readingisgood.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ataatasoy.readingisgood.controllers.OrderController;
import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.models.OrderStatus;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

  @Override
  public EntityModel<Order> toModel(Order order) {

    EntityModel<Order> orderModel = EntityModel.of(order, //
        linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
        linkTo(methodOn(OrderController.class).all()).withRel("orders"));

    if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
      orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
      orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
    }

    return orderModel;

  }
}