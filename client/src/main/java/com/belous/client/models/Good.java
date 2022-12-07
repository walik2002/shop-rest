package com.belous.client.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Good {

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
