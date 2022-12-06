package com.belous.api.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class OrderGood {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime orderDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime orderCompletionDate;

    @ManyToMany
    private List<Good> goods = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    public OrderGood(Status status, LocalDateTime orderDate, LocalDateTime orderCompletionDate, List<Good> goods, Employee employee) {
        this.status = status;
        this.orderDate = orderDate;
        this.orderCompletionDate = orderCompletionDate;
        this.goods = goods;
        this.employee = employee;
    }

    public enum Status{
        WAITING,
        PROCESSING,
        CANCELED,
        DONE
    }
}
