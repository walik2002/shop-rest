package com.belous.client.services;

import com.belous.client.models.Good;
import com.belous.client.models.OrderGood;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@NoArgsConstructor
public class OrderService {

    private final String URL = "http://localhost:5000/api/orders";
    private RestTemplate restTemplate = new RestTemplate();
    List<Good> goodList = List.of(

    );
    OrderGood orderGood = new OrderGood(OrderGood.Status.WAITING, LocalDateTime.now(),LocalDateTime.now(),goodList);

    public OrderService(@CookieValue(name = "access_token") String accessToken) {
        this.restTemplate = new RestTemplate();
        if (accessToken != null) {
            this.restTemplate
                    .getInterceptors()
                    .add(getBearerTokenInterceptor(accessToken));
        }
    }

    public Iterable<OrderGood> getUserOrders(String username){
        return Arrays.asList(restTemplate.getForObject(
                URL+"/" +username,
                OrderGood[].class));
    }

    public String saveOrder(String username){
       return restTemplate.postForObject(URL +"/" +username,orderGood,String.class);
    }

    private ClientHttpRequestInterceptor
    getBearerTokenInterceptor(String accessToken) {
        ClientHttpRequestInterceptor interceptor =
                new ClientHttpRequestInterceptor() {
                    @Override
                    public ClientHttpResponse intercept(
                            HttpRequest request, byte[] bytes,
                            ClientHttpRequestExecution execution) throws IOException {
                        request.getHeaders().add("Authorization", "Bearer " + accessToken);
                        return execution.execute(request, bytes);
                    }
                };

        return interceptor;
    }
}
