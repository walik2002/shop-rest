package com.belous.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String showHomePage(@CookieValue(name = "username",defaultValue = "") String username, Model model){

        if(username !="")
        {model.addAttribute("username",username);}
        return "home";
    }
}
