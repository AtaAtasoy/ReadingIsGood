package com.ataatasoy.readingisgood.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public abstract class AggregateEntity {

    @Nullable
    @Id
    @JsonIgnore
    private Long id;
}
