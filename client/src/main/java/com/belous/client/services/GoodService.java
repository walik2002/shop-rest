package com.belous.client.services;

import com.belous.client.models.Good;

public interface GoodService {
    Iterable<Good> findAll();

    Good getOne(String goodId);

    Good addGood(Good good);

    void deleteGood(String good);

    void editGood(String goodId,Good good);
}
