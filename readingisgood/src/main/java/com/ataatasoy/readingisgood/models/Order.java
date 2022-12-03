package com.ataatasoy.readingisgood.models;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    private @Id @GeneratedValue long id;
    private @CreationTimestamp Date createdAt;

    @ManyToMany(targetEntity = com.ataatasoy.readingisgood.models.Book.class, cascade = CascadeType.ALL)
    private @NonNull List<Book> books;
}