package com.belous.api.repos;

import com.belous.api.models.Good;
import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<Good,Long> {
}
