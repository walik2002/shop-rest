package com.belous.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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

}
