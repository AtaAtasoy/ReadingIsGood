package com.ataatasoy.readingisgood.repository;

import com.ataatasoy.readingisgood.models.Statistics;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class StatisticsRepository{
    @PersistenceContext
    EntityManager entityManager;

    public List<Statistics> findMonthlyStatisticsForCustomer(Long customerId){
        Query q = entityManager.createNativeQuery("SELECT rand() as id, COUNT(DISTINCT order_id) AS total_order_count,SUM(quantity) AS total_book_count," +
                            "MONTHNAME(created_at) as month_name,SUM(price*quantity) AS total_purchased_amount "+
                            "FROM (SELECT od.order_id, o.created_at, od.quantity, od.price FROM order_detail od " +
                            "INNER JOIN ORDERS o ON o.order_id = od.order_id " +
                            "WHERE o.customer_id = :customerId) " +
                            "GROUP BY MONTHNAME(created_at)", Statistics.class).setParameter("customerId", customerId);
        System.out.println(q.getSingleResult());

        return (List<Statistics>) q.getResultList();
    }
}
