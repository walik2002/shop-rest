package com.belous.api.controllers;

import com.belous.api.models.RoleEntity;
import com.belous.api.models.UserEntity;
import com.belous.api.repos.UserJpaRepository;
import com.belous.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RestController
@RequestMapping("registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping
    public String registrationUser(@RequestBody UserEntity user, HttpServletResponse response){

        if(userService.findByUsername(user.getUsername())!=null){
            throw new RuntimeException("Пользователь с таким логином уже существует");
        } else{
            userService.save(user);
            userService.addRoleToUser(user.getUsername(),"ROLE_USER");
            return  "Пользователь успешно создан";
        }

    };
}
