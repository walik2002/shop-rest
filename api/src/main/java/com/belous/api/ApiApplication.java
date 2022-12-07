package com.belous.api;

import com.belous.api.models.Good;
import com.belous.api.repos.GoodRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public ApplicationRunner dataLoader(
            GoodRepository goodRepository
    )
    {
        return args -> {
            Good [] goods = new Good[]{
                            new Good("Видеокарта", 3,new BigDecimal(100.50)),
                            new Good("Процессор", 6,new BigDecimal(60.50)),
                            new Good("Материнская плата", 4,new BigDecimal(80.50))};
            goodRepository.saveAll(Arrays.asList(goods));
        };
    }

}
