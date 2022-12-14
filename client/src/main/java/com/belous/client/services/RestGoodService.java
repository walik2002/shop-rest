package com.belous.client.services;

import com.belous.client.models.Good;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
@Service
@NoArgsConstructor
public class RestGoodService implements GoodService{

    private final String URL = "http://localhost:5000/api/goods";
    private RestTemplate restTemplate = new RestTemplate();

    public RestGoodService(@CookieValue(name = "access_token") String accessToken) {
        this.restTemplate = new RestTemplate();
        if (accessToken != null) {
            this.restTemplate
                    .getInterceptors()
                    .add(getBearerTokenInterceptor(accessToken));
        }
    }

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
