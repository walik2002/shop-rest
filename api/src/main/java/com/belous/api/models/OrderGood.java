package com.belous.api.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JsonIgnore
    private UserEntity user;

    @Column(name = "user_id")
    private Long userId;

    public OrderGood(Status status, LocalDateTime orderDate, LocalDateTime orderCompletionDate, List<Good> goods, UserEntity user) {
        this.status = status;
        this.orderDate = orderDate;
        this.orderCompletionDate = orderCompletionDate;
        this.goods = goods;
        this.user = user;
    }

    public enum Status{
        WAITING,
        PROCESSING,
        CANCELED,
        DONE
    }
}
