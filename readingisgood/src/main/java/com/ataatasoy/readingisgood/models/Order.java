package com.ataatasoy.readingisgood.models;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.ataatasoy.readingisgood.assemblers.BookModelAssembler;
import com.ataatasoy.readingisgood.controllers.BookController;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, 
                  property  = "id", 
                  scope     = Order.class)

public class Order{
    private @Id @GeneratedValue Long id;
    
    private  @CreationTimestamp @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt;
    private @lombok.NonNull Status status;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "ordered_books", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> orderedBooks = new ArrayList<>();

    public void addBook(Book book) {
        orderedBooks.add(book);
    }
}