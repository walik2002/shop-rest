package com.belous.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodId;

    private String name;
    private int count;

    private BigDecimal price;
}
