package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.models.Book;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.repository.CustomerRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.validation.Validator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Validator.class, CustomerService.class, CustomerRepository.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CustomerServiceTest {
    @Autowired
    CustomerService service;
    @Autowired
    CustomerRepository repository;
    @Autowired
    Validator validator;

    @Test
    void getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        Customer c1 = new Customer("Dogac", "Eldenk", "dogac@email.com");
        Customer c2 = new Customer("Mert", "Aslan", "mert@email.com");

        list.add(c1);
        list.add(c2);

        when(repository.findAll()).thenReturn(list);

        List<Customer> bookList = service.getAllCustomers();
        assertEquals(2, bookList.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void addNewCustomer() {
        Customer customer = new Customer("Dogac", "Eldenk", "dogac@email.com");

        when(repository.save(customer)).thenReturn(customer);
        Customer expected = service.addNewCustomer(customer);

        assertEquals("Dogac", expected.getName());
        assertEquals("Eldenk", expected.getEmail());
        assertEquals("dogac@email.com", expected.getEmail());

        verify(repository, times(1)).save(customer);
    }

    @Test
    void getCustomer() {
    }
}