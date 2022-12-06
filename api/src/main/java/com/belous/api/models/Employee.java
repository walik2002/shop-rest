package com.belous.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employeeId;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    @OneToMany(mappedBy = "employee", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OrderGood> orderGoods = new ArrayList<>();

    @OneToOne
    private User user;
}
