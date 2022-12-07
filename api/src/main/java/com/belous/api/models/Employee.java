package com.belous.api.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
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

    public Employee(String firstName, String lastName, String address, String phoneNumber, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }
}
