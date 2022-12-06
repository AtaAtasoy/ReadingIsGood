package com.ataatasoy.readingisgood.models;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(OrderDetailPK.class)
public class OrderDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Order order;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    @JsonProperty("paid_price")
    private Double price;
}
