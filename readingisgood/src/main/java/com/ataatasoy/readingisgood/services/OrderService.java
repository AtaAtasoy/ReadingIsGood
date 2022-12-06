package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.exceptions.*;
import com.ataatasoy.readingisgood.models.*;
import com.ataatasoy.readingisgood.repository.OrderRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class OrderService {

    @Autowired
    private Validator validator;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookService bookService;

    private void validateOrder(Order order) {
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Order> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new InvalidCustomerException("Error occurred: " + sb.toString());
        }
    }

    public Order getOrder(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    public List<Order> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty())
            throw new OrdersDoNotExistException();
        return orders;
    }

    public Order addNewOrder(Order newOrder){
        newOrder.setOrderStatus(OrderStatus.IN_PROGRESS);
        List<Book> parsedBooks = new ArrayList<>();

        // Update stock
        for (Book bookInOrder : newOrder.getOrderedBooks()) {
            Book savedBook = bookService.getBook(bookInOrder.getId());

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
        Customer savedCustomer = customerService.getCustomer(newOrder.getCustomer().getId());
        newOrder.setCustomer(savedCustomer);
        newOrder.getCustomer().addOrder(newOrder);

        validateOrder(newOrder);
        return orderRepository.save(newOrder);
    }

    public List<Order> getOrdersBetweenDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime end)
    {
       List<Order> ordersInRange = orderRepository.findByCreatedAtBetween(start, end);
       if (ordersInRange.isEmpty()){
           throw new OrderNotFoundInRangeException(start, end);
       }
       return ordersInRange;
    }

    public List<Order> getPagedOrdersOfCustomer(Long customerId, int offset, int pageSize){
        Pageable paging = PageRequest.of(offset, pageSize, Sort.by("createdAt"));
        List<Order> pagedResult = orderRepository.findByCustomerId(customerId, paging);
        if (pagedResult.isEmpty()){
            throw new OrdersDoNotExistException();
        }
        return pagedResult;
    }
}
