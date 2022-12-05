package com.ataatasoy.readingisgood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ataatasoy.readingisgood.models.OrderQuantity;
import com.ataatasoy.readingisgood.models.OrderQuantityKey;

public interface OrderQuantityRepository extends JpaRepository<OrderQuantity, OrderQuantityKey> {
    
}
