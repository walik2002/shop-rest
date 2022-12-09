package com.belous.api.repos;

import com.belous.api.models.OrderGood;
import com.belous.api.models.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderGoodRepository extends CrudRepository<OrderGood,Long> {
    Iterable<OrderGood> findOrderGoodsByUser(UserEntity user);
}
