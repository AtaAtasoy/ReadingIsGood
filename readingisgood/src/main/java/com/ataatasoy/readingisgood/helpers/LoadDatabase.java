package com.ataatasoy.readingisgood.helpers;


import com.ataatasoy.readingisgood.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ataatasoy.readingisgood.repository.BookRepository;
import com.ataatasoy.readingisgood.repository.CustomerRepository;
import com.ataatasoy.readingisgood.repository.OrderDetailRepository;
import com.ataatasoy.readingisgood.repository.OrderRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CustomerRepository cRepository, BookRepository bRepository,
            OrderRepository oRepository, OrderDetailRepository oqRepository) {
        Book b1 = new Book();
        b1.setName("1984");
        b1.setAuthor("George Orwell");
        b1.setPrice(32.0);
        b1.setStock(20);
        b1.setQuantity(0);

        Book b2 = new Book();
        b2.setName("Harry Potter and the Prisoner of Azkaban"); 
        b2.setAuthor("J.K. Rowling");
        b2.setPrice(32.9);
        b2.setStock(29);
        b2.setQuantity(0);
        
        Customer c1 = new Customer();
        c1.setName("Doga");
        c1.setSurname("Erdem");
        c1.setEmail("doga@email.com");
        Order o1 = new Order(OrderStatus.IN_PROGRESS);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(3);
        orderDetail.setBook(b1);
        orderDetail.setOrder(o1);
        orderDetail.setPrice(b1.getPrice());

        b1.addQuantity(orderDetail);
        o1.addQuantity(orderDetail);;

        b1.addToOrder(o1);
        b2.addToOrder(o1);
        o1.addBook(b1);
        o1.addBook(b2);
        
        o1.setCustomer(c1);
        c1.addOrder(o1);
        return args -> {
            log.info("Preloading " + cRepository.save(c1));
            log.info("Preloading " + bRepository.save(b1));
            log.info("Preloading " + bRepository.save(b2));
            log.info("Preloading " + oRepository.save(o1));
            log.info("Preloading " + oqRepository.save(orderDetail));
        };
    }

}
