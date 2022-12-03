package com.ataatasoy.readingisgood.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.ataatasoy.readingisgood.assemblers.CustomerModelAssembler;
import com.ataatasoy.readingisgood.assemblers.OrderModelAssembler;
import com.ataatasoy.readingisgood.exceptions.CustomerAlreadyExistsException;
import com.ataatasoy.readingisgood.exceptions.CustomerNotFoundException;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.repository.CustomerRepository;

import lombok.Data;

@Data
@RestController
public class CustomerController {
    private final CustomerRepository repository;
    private final CustomerModelAssembler cModelAssembler;
    private final OrderModelAssembler oModelAssembler;

    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> all() {
        List<EntityModel<Customer>> customers = repository.findAll().stream() //
                .map(cModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    @PostMapping("/customers")
    ResponseEntity<?> newCustomer(@RequestBody Customer newCustomer){
        try {
            EntityModel<Customer> entityModel = cModelAssembler.toModel(repository.save(newCustomer));

            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (DataIntegrityViolationException e) {
            throw new CustomerAlreadyExistsException(newCustomer.getEmail());
        }
        
    }


    @GetMapping("/customers/{id}")
    public EntityModel<Customer> one(@PathVariable Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        return cModelAssembler.toModel(customer);
    }

    //TODO: Index and PageSize checks
    //TODO: Add 'total' and 'page' fields to response
    @GetMapping("/customers/{id}/orders")
    CollectionModel<EntityModel<Order>> allOrders(@PathVariable Long id, @RequestParam(required = false) int offset, @RequestParam(required = false) int pageSize) {
        Customer c = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        List<EntityModel<Order>> orders = c.getOrderList().subList(offset, pageSize).stream()
                .map(oModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }
}
