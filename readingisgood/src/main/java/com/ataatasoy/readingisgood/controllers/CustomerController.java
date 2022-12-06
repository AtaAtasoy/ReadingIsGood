package com.ataatasoy.readingisgood.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.ataatasoy.readingisgood.services.CustomerService;
import com.ataatasoy.readingisgood.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.ataatasoy.readingisgood.exceptions.InvalidCustomerException;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.models.Order;

@RestController
public class CustomerController {

    @Autowired
    private CustomerModelAssembler customerModelAssembler;
    @Autowired
    private OrderModelAssembler orderModelAssembler;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> all() {
        List<Customer> customers = customerService.getAllCustomers();
        List<EntityModel<Customer>> customerModels = customers.stream() //
                .map(customerModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Customer>> customerModelCollection = CollectionModel.of(customerModels,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(customerModelCollection);
    }

    @PostMapping("/customers")
    ResponseEntity<EntityModel<Customer>> newCustomer(@RequestBody Customer newCustomer){
        Customer savedCustomer = customerService.addNewCustomer(newCustomer);
        try {
            EntityModel<Customer> entityModel = customerModelAssembler.toModel(savedCustomer);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } catch (IllegalArgumentException e) {
            throw new InvalidCustomerException(newCustomer.getEmail());
        }
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<EntityModel<Customer>> one(@PathVariable Long id) {
        Customer customer = customerService.getCustomer(id);

        EntityModel<Customer> entityModel = customerModelAssembler.toModel(customer);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    //TODO: Index and PageSize checks
    @GetMapping("/customers/{id}/orders")
    ResponseEntity<CollectionModel<EntityModel<Order>>> allOrders(@PathVariable Long id, @RequestParam int offset,
                                                                  @RequestParam int pageSize) {
        List<EntityModel<Order>> orders = orderService.getPagedOrdersOfCustomer(id, offset, pageSize).stream()
                .map(orderModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Order>> collectionModel = CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());

        return  ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }
}
