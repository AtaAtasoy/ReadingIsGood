package com.ataatasoy.readingisgood.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="books")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Book.class)
@JsonIgnoreProperties("ordersIncludedIn")
public class Book {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(unique = true)
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "Author must not be blank")
    private String author;
    private @CreationTimestamp @Column(updatable = false) Date createdAt;
    private @UpdateTimestamp Date lastUpdatedAt;
    @Min(value = 0, message = "Stock should not be less than 0")

    private Integer stock;
    @Min(value = 0, message = "Price should not be less than 18")
    private Double price;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer quantity = 0;

    @ManyToMany(mappedBy = "orderedBooks", cascade = CascadeType.ALL)
    private List<Order> ordersIncludedIn = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<OrderDetail> ordersDetailed = new ArrayList<>();

    public void addToOrder(Order order) {
        ordersIncludedIn.add(order);
    }

    public void addQuantity(OrderDetail orderDetail){
        ordersDetailed.add(orderDetail);
    }
}