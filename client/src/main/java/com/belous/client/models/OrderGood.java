package com.belous.client.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class OrderGood {

    private Long orderId;

    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime orderDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime orderCompletionDate;

    private List<Good> goods = new ArrayList<>();

    public OrderGood(Status status, LocalDateTime orderDate, LocalDateTime orderCompletionDate, List<Good> goods) {
        this.status = status;
        this.orderDate = orderDate;
        this.orderCompletionDate = orderCompletionDate;
        this.goods = goods;
    }

    public enum Status{
        WAITING,
        PROCESSING,
        CANCELED,
        DONE
    }

    public void addGood(Good good){
        this.goods.add(good);
    }
}