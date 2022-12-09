package com.belous.client.controllers;

import com.belous.client.models.OrderGood;
import com.belous.client.services.OrderService;
import com.belous.client.services.RestGoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public String showOrdersPage(Model model, @CookieValue(name = "access_token",defaultValue = "") String accessToken,
                                 @ModelAttribute("currentOrder") OrderGood orderGood,@CookieValue(name = "username") String username){
        this.orderService = new OrderService(accessToken);
        Iterable<OrderGood> userOrders = orderService.getUserOrders(username);
        model.addAttribute("userOrders",userOrders);
        return "orders";
    }

    @PostMapping
    public String save(@CookieValue(name = "username") String username){
        orderService.saveOrder(username);
        return "redirect:/orders";
    }
}
