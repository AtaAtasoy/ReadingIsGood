package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.exceptions.CustomerNotFoundException;
import com.ataatasoy.readingisgood.exceptions.InvalidCustomerException;
import com.ataatasoy.readingisgood.models.Customer;
import com.ataatasoy.readingisgood.repository.CustomerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService {
    @Autowired
    private Validator validator;
    @Autowired
    private CustomerRepository repository;

    private void validateCustomer(Customer customer) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Customer> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new InvalidCustomerException("Error occurred: " + sb.toString());
        }
    }

    public List<Customer> getAllCustomers(){
        return repository.findAll();
    }

    public Customer addNewCustomer(Customer newCustomer){
        validateCustomer(newCustomer);
        try {
            return repository.save(newCustomer);
        } catch (Exception e) {
            throw new InvalidCustomerException("Could not save the input. email might be taken." + e.getMessage());
        }
    }

    public Customer getCustomer(Long id){
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
