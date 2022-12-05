package com.ataatasoy.readingisgood.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private @Column(unique = true) String name;
    private String author;
    private @CreationTimestamp @Column(updatable = false) Date createdAt;
    private @UpdateTimestamp Date lastUpdatedAt;
    private Integer stock;
    private Double price;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer quantity = 0;

    @ManyToMany(mappedBy = "orderedBooks", cascade = CascadeType.ALL)
    private List<Order> ordersIncludedIn = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<OrderQuantity> quantities = new ArrayList<>();

    public void addToOrder(Order order) {
        ordersIncludedIn.add(order);
        /** 
        OrderQuantity oq = new OrderQuantity();
        oq.setBook(this);
        oq.setOrder(order);
        oq.setQuantity(quantity);
        

        quantities.add(oq);*/
    }

    public void addQuantity(OrderQuantity quantity){
        quantities.add(quantity);
    }
}