package com.belous.client.controllers;

import com.belous.client.models.OrderGood;
import com.belous.client.services.OrderService;
import com.belous.client.services.RestGoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@SessionAttributes("currentOrder")
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public String showOrdersPage(Model model,
                                 @CookieValue(name = "access_token",defaultValue = "") String accessToken,
                                 @CookieValue(name = "username") String username){

        this.orderService = new OrderService(accessToken);
        Iterable<OrderGood> userOrders = orderService.getUserOrders(username);
        model.addAttribute("userOrders", OrderGood.sortByDate((List<OrderGood>) userOrders));
        return "orders";

    }

    @PostMapping
    public String saveOrder(@CookieValue(name = "username") String username,@ModelAttribute("currentOrder") OrderGood currentOrder,SessionStatus status){
        orderService.saveOrder(username,currentOrder);
        status.setComplete();
        return "redirect:/orders";
    }

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        OrderGood canceledOrder = orderService.getById(orderId);
        canceledOrder.setStatus(OrderGood.Status.CANCELED);
        orderService.cancelOrder(canceledOrder);
        return "redirect:/orders";
    }

    @PostMapping("/done/{orderId}")
    public String doneOrder(@PathVariable("orderId") Long orderId){
        OrderGood canceledOrder = orderService.getById(orderId);
        canceledOrder.setStatus(OrderGood.Status.DONE);
        orderService.cancelOrder(canceledOrder);
        return "redirect:/orders";
    }
}
