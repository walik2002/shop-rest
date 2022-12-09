package com.belous.api.models;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodId;

    private String name;
    private int count;

    private BigDecimal price;

    public Good( String name, int count, BigDecimal price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }
}
