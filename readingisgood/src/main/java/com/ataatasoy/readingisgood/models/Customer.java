package com.ataatasoy.readingisgood.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customers")
public class Customer{
    private @Id @GeneratedValue Long id; 
    private @NonNull String name;
    private @NonNull String surname;
    private @NonNull @Column(unique = true) String email;

    @OneToMany(targetEntity = com.ataatasoy.readingisgood.models.Order.class, cascade = CascadeType.ALL)
    private @NonNull List<Order> orderList;
}
