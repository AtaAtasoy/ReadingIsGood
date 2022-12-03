package com.ataatasoy.readingisgood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ataatasoy.readingisgood.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
