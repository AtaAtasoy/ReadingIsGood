package com.ataatasoy.readingisgood.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
public class OrderQuantityKey implements Serializable {
    private Long order;
    private Long book;
}
