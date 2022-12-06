package com.belous.api.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clientId;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    @OneToOne
    private User user;

    public Client(String firstName, String lastName, String address, String phoneNumber, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }
}
