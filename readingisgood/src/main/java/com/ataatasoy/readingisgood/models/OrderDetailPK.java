package com.ataatasoy.readingisgood.models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrderDetailPK implements Serializable {
    private Long order;
    private Long book;
}
