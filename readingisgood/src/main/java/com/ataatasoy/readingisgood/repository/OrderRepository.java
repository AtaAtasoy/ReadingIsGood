package com.ataatasoy.readingisgood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ataatasoy.readingisgood.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
