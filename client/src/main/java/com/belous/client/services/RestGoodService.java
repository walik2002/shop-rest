package com.belous.client.services;

import com.belous.client.models.Good;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
@Service
public class RestGoodService implements GoodService{

    private final String URL = "http://localhost:5000/api/goods";
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Iterable<Good> findAll() {
        return Arrays.asList(restTemplate.getForObject(
                URL,
                Good[].class));
    }

    @Override
    public Good getOne(String goodId) {
        return restTemplate.getForObject(URL+"/"+goodId,
                Good.class);
    }

    @Override
    public Good addGood(Good good) {
        return restTemplate.postForObject(
                URL, good, Good.class);
    }

    @Override
    public void deleteGood(String id) {
        restTemplate.delete(URL + "/" + id);
    }

    @Override
    public void editGood(String goodId, Good good) {
        restTemplate.put(URL+"/"+goodId,
                good,
                Good.class);
    }
}
