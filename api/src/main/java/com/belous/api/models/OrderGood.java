package com.belous.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class OrderGood {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    private Status status;

    LocalDateTime orderDate;

    LocalDateTime orderCompletionDate;

    @ManyToMany
    private List<Good> goods = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    public enum Status{
        WAITING,
        PROCESSING,
        CANCELED,
        DONE
    }
}
