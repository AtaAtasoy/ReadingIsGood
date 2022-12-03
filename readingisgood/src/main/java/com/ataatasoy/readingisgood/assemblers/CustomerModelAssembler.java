package com.ataatasoy.readingisgood.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ataatasoy.readingisgood.controllers.CustomerController;
import com.ataatasoy.readingisgood.models.Customer;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

  @Override
  public EntityModel<Customer> toModel(Customer customer) {

    return EntityModel.of(customer, //
        linkTo(methodOn(CustomerController.class).one(customer.getId())).withSelfRel(),
        linkTo(methodOn(CustomerController.class).all()).withRel("customers"));
  }
}