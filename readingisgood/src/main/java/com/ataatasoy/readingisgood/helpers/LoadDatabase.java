package com.ataatasoy.readingisgood.helpers;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.models.Order;
import com.ataatasoy.readingisgood.models.Status;
import com.ataatasoy.readingisgood.repository.BookRepository;
import com.ataatasoy.readingisgood.repository.CustomerRepository;
import com.ataatasoy.readingisgood.repository.OrderRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CustomerRepository cRepository, BookRepository bRepository,
            OrderRepository oRepository) {
        Book b1 = new Book("1984", "George Orwell");
        Book b2 = new Book("Harry Potter and the Prisoner of Azkaban", "J.K. Rowling");
        
        Customer c1 = new Customer("Doga", "Erdem", "doga@email.com");
        Order o1 = new Order(Status.IN_PROGRESS);
        
        b1.addToOrder(o1);
        b2.addToOrder(o1);
        o1.addBook(b1);
        o1.addBook(b2);

        o1.setCustomer(c1);
        c1.addOrder(o1);

        return args -> {
            log.info("Preloading " + bRepository.save(b1));
            log.info("Preloading " + bRepository.save(b2));
            log.info("Preloading " + cRepository.save(c1));
            log.info("Preloading " + oRepository.save(o1));
          
        };
    }

}
