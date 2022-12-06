package com.ataatasoy.readingisgood.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "order_id")
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private  @CreationTimestamp @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt;
    private @lombok.NonNull OrderStatus orderStatus;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ordered_books", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Book> orderedBooks = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonProperty("orderDetails")
    private List<OrderDetail> quantities = new ArrayList<>();

    public void addBook(Book book) {
        orderedBooks.add(book);
    }

    public void addQuantity(OrderDetail quantity){
        quantities.add(quantity);
    }
}