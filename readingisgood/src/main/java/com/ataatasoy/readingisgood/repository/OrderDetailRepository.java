package com.ataatasoy.readingisgood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ataatasoy.readingisgood.models.OrderDetail;
import com.ataatasoy.readingisgood.models.OrderDetailPK;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPK> {
    
}
