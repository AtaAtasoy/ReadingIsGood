package com.ataatasoy.readingisgood.repository;

import com.ataatasoy.readingisgood.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ataatasoy.readingisgood.models.OrderDetail;
import com.ataatasoy.readingisgood.models.OrderDetailPK;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPK> {

    @Query(value = "SELECT COUNT(DISTINCT order_id) AS \"totalOrderCount\",SUM(quantity) AS \"totalNumberOfBooks\"," +
            "MONTHNAME(created_at) as \"monthName\",SUM(price*quantity) AS \"totalPrice\" " +
            "FROM (SELECT od.order_id, o.created_at, od.quantity, od.price FROM order_detail od " +
            "INNER JOIN ORDERS o ON o.order_id = od.order_id " +
            "WHERE o.customer_id = :customerId)" +
            "GROUP BY MONTHNAME(created_at)", nativeQuery = true)
    Set<Statistics> getMonthlyCustomerStatistics(Long customerId);
}
