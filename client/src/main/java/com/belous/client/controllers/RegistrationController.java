package com.belous.client.controllers;

import com.belous.client.models.User;
import com.belous.client.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @ModelAttribute(name = "registrationUser")
    public User getUser(){
        return new User();
    }

    @ModelAttribute(name = "registrationMessage")
    public String authEx(){
        return null;
    }

    @GetMapping
    public String showRegistrationPage(User registrationUser, Model model){
        model.addAttribute("registrationUser",registrationUser);
        return "registration";
    }
    @PostMapping
    public String registration(@ModelAttribute User registrationUser, HttpServletResponse response, Model model,@ModelAttribute String registrationMessage){
       Map<String,String> registrationMessages =  registrationService.registration(registrationUser);
        if (registrationMessages.containsKey("success")) {
            registrationMessage = registrationMessages.get("success");
            model.addAttribute("registrationMessage",registrationMessage);
            model.addAttribute("appUser",registrationUser);
            return "login";
        } else {
            registrationMessage = registrationMessages.get("error");
            model.addAttribute("registrationMessage",registrationMessage);
            return "registration";
        }

    }
}
