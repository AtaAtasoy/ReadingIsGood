package com.ataatasoy.readingisgood.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
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
import com.ataatasoy.readingisgood.repository.OrderRepository;

import lombok.Data;

@Data
@RestController
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerModelAssembler customerModelAssembler;
    private final OrderModelAssembler orderModelAssembler;

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> all() {
        List<EntityModel<Customer>> customers = customerRepository.findAll().stream() //
                .map(customerModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Customer>> customerModels = CollectionModel.of(customers, linkTo(methodOn(CustomerController.class).all()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(customerModels);
    }

    @PostMapping("/customers")
    ResponseEntity<EntityModel<Customer>> newCustomer(@RequestBody Customer newCustomer){
        try {
            EntityModel<Customer> entityModel = customerModelAssembler.toModel(customerRepository.save(newCustomer));
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (DataIntegrityViolationException e) {
            throw new CustomerAlreadyExistsException(newCustomer.getEmail());
        }
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<EntityModel<Customer>> one(@PathVariable Long id) {
        try{
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
            EntityModel<Customer> entityModel = customerModelAssembler.toModel(customer);
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);
        } catch (IllegalArgumentException e){
            throw new CustomerNotFoundException(id);
        }
    }

    //TODO: Index and PageSize checks
    @GetMapping("/customers/{id}/orders")
    ResponseEntity<CollectionModel<EntityModel<Order>>> allOrders(@PathVariable Long id, @RequestParam(required = false) int offset, @RequestParam(required = false) int pageSize) {
        try{
            Customer c = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
            Pageable paging = PageRequest.of(offset, pageSize, Sort.by("createdAt"));
            List<Order> pagedResult = orderRepository.findByCustomerId(c.getId(), paging);

            List<EntityModel<Order>> orders = pagedResult.stream()
                    .map(orderModelAssembler::toModel)
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<Order>> collectionModel = CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());

            return  ResponseEntity.status(HttpStatus.OK).body(collectionModel);
        } catch (IllegalArgumentException e){
            throw new CustomerNotFoundException(id);
        }
    }
}
