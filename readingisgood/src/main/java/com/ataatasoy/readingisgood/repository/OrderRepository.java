package com.ataatasoy.readingisgood.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import com.ataatasoy.readingisgood.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(long customerId, Pageable pageable);

    @Query("select o from Order o where o.createdAt >= :start and o.createdAt <= :end ")
    List<Order> findByCreatedAtBetween(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("start") LocalDateTime start,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("end") LocalDateTime end);
}
