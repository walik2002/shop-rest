package com.belous.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String username;

    private String password;

    private Role role;

    public enum Role{
        CLIENT,
        ADMIN
    }
}
