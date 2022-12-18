package com.belous.client.services;

import com.belous.client.models.User;
import org.apache.tomcat.util.http.parser.Cookie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.Map;

@Service
public class LoginService {

    private final String URL = "http://localhost:5000/login";
    private RestTemplate restTemplate = new RestTemplate();


    public Map<String,String> login(User user){

        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> response = null;
        try {
            response = restTemplate
                    .exchange(URL, HttpMethod.POST, request, User.class);
        } catch (RestClientException e) {
            return Map.of("error","Неправильные учетные данные");
        }

        return Map.of("access_token",response.getHeaders().getFirst("access_token"),
                "username",response.getHeaders().getFirst("username"),
                "roles",response.getHeaders().getFirst("roles"));
    }
}
