package com.belous.client.controllers;

import com.belous.client.models.User;
import com.belous.client.services.LoginService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @ModelAttribute(name = "appUser")
    public User getUser(){
        return new User();
    }

    @GetMapping
    public String showLoginPage(User appUser, Model model){
        model.addAttribute("appUser",appUser);
        return "login";
    }

    @ModelAttribute(name = "authEx")
    public String authEx(){
        return null;
    }

    @PostMapping
    public String login(@ModelAttribute User appuser, HttpServletResponse response,Model model,@ModelAttribute String authEx){
        Map<String,String> headers = loginService.login(appuser);
        if (!headers.containsKey("error")) {
            Cookie token_cookie = new Cookie("access_token",headers.get("access_token"));
            Cookie username_cookie = new Cookie("username",headers.get("username"));
            response.addCookie(token_cookie);
            response.addCookie(username_cookie);
        } else {
            authEx = headers.get("error");
            model.addAttribute("authEx",authEx);
            return "login";
        }

        return "redirect:/";
    }

}
