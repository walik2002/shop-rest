package com.belous.api;

import com.belous.api.models.Good;
import com.belous.api.models.RoleEntity;
import com.belous.api.models.UserEntity;
import com.belous.api.repos.GoodRepository;
import com.belous.api.services.RoleService;
import com.belous.api.services.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner dataLoader(
            GoodRepository goodRepository,
            UserService userService, RoleService roleService
    )
    {
        return args -> {
            Good [] goods = new Good[]{
                            new Good("Видеокарта", 3,new BigDecimal(100.50)),
                            new Good("Процессор", 6,new BigDecimal(60.50)),
                            new Good("Материнская плата", 4,new BigDecimal(80.50))};
            goodRepository.saveAll(Arrays.asList(goods));

            roleService.save(new RoleEntity(null, "ROLE_USER"));
            roleService.save(new RoleEntity(null, "ROLE_ADMIN"));

            userService.save(new UserEntity(null, "admin", "1234", new ArrayList<>()));
            userService.save(new UserEntity(null, "user", "1234", new ArrayList<>()));

            userService.addRoleToUser("user", "ROLE_USER");
            userService.addRoleToUser("admin", "ROLE_ADMIN");
            userService.addRoleToUser("admin", "ROLE_USER");
        };
    }

}
