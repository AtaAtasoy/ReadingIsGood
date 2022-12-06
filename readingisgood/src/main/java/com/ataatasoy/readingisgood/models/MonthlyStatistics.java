package com.ataatasoy.readingisgood.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class MonthlyStatistics {
    private @Id Long id;
    private String monthName;
    private Integer totalOrderCount;
    private Integer totalBookCount;
    private Double totalPurchasedAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
