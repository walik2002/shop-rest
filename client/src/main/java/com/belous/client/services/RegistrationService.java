package com.belous.client.services;

import com.belous.client.models.User;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service

public class RegistrationService {
    private final String URL = "http://localhost:5000/registration";
    private RestTemplate restTemplate = new RestTemplate();

    public Map<String,String> registration(User user){

        HttpEntity<User> request = new HttpEntity<>(user);
        String response = "";
        try {
            response = restTemplate.postForObject(URL,user,String.class);
        } catch (RuntimeException e) {
            return Map.of("error","Пользователь с таким логином уже существует");
        }

        return Map.of("success",response);
    }
}
