package com.ataatasoy.readingisgood.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Statistics extends AggregateEntity {
    @Column(name="month_name")
    private String monthName;
    @Column(name="total_order_count")
    private Integer totalOrderCount;
    @Column(name="total_book_count")
    private Integer totalBookCount;
    @Column(name="total_purchased_amount")
    private Double totalPurchasedAmount;

}
