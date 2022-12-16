package com.belous.client.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class OrderGood {

    private Long orderId;

    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime orderDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime orderCompletionDate;

    private List<Good> goods;

    private Long userId;

    private User user;

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
        if(goods==null){
            goods = new ArrayList<>();
        }
        this.goods.add(good);
    }
    public String totalPrice(){
        BigDecimal total = new BigDecimal(0);
        for(Good good : goods){
            total = total.add(good.getPrice());
        }
        return total.toString();
    }

    public String getStatusString() {
        return status.toString();
    }
    public boolean isGoodPresent(Long id){
        for(Good good: goods){
            if(good.getGoodId().equals(id))
                return true;
        }
        return false;
    }

    public static Iterable<OrderGood> sortByDate(List<OrderGood> goods){
        return goods
                .stream()
                .sorted(Comparator.comparing(OrderGood::getOrderDate).reversed())
                .collect(Collectors.toList());
    }
}