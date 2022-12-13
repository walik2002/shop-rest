package com.belous.client.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String username;
    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    public User(String username, String password, String firstName, String lastName, String address, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
