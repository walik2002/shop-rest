package com.belous.api.repos;

import com.belous.api.models.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);
}
