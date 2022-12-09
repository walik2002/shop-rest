package com.belous.client.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleException(HttpClientErrorException e){
        return "redirect:/login";
    }
}
