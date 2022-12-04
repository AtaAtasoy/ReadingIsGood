package com.ataatasoy.readingisgood.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, 
                  property  = "id", 
                  scope     = Book.class)
@JsonIgnoreProperties("ordersIncludedIn")
public class Book{
    private @Id @GeneratedValue Long id;
    private @NonNull @Column(unique = true) String name;
    private @NonNull String author;
    private @CreationTimestamp @Column(updatable = false) Date createdAt;
    private @UpdateTimestamp Date lastUpdatedAt;
    private int stock;
    private double price;
    
    @ManyToMany(mappedBy = "orderedBooks")
    private List<Order> ordersIncludedIn = new ArrayList<>();

    public void addToOrder(Order order){
        ordersIncludedIn.add(order);
    }
}