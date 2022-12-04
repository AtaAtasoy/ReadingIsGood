package com.ataatasoy.readingisgood.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    private @Id @GeneratedValue Long id;
    private @NonNull @Column(unique = true) String name;
    private @NonNull String author;
    private @CreationTimestamp @Column(updatable = false) Date createdAt;
    private @UpdateTimestamp Date lastUpdatedAt;
    private int stock;
    private double price;
    
    @ManyToMany(mappedBy = "orderedBooks")
    private @NonNull List<Order> ordersIncludedIn;

    public void addToOrder(Order order){
        ordersIncludedIn.add(order);
    }
}