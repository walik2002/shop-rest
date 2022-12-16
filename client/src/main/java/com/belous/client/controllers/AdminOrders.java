package com.belous.client.controllers;

import com.belous.client.models.OrderCompletionDate;
import com.belous.client.models.OrderGood;
import com.belous.client.models.User;
import com.belous.client.services.OrderService;
import com.belous.client.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrders {
    private OrderService orderService;

    private UserService userService;

    private String statusFilter = "WAITING";

    @GetMapping()
    public String showAdminOrdersPage(){

        return "adminOrders";
    }

    @ModelAttribute
    public void addStatusesToModel(Model model){
        OrderGood.Status[] statuses = OrderGood.Status.values();

        List<String> stringStatuses = new ArrayList<>();

        for (OrderGood.Status status : statuses){
            stringStatuses.add(status.toString());
        }

        model.addAttribute("statuses",stringStatuses);
    }

    @ModelAttribute
    public void addOrdersToModel(Model model,@CookieValue(name = "access_token",defaultValue = "") String accessToken){
        this.orderService = new OrderService(accessToken);
        this.userService = new UserService(accessToken);
        Iterable<OrderGood> orders = orderService.getAllOrders();

        Iterable<User> users = userService.getAllUsers();

        for (OrderGood order : orders){
            Long userId = order.getUserId();
            for (User user: users){
                if(user.getId().equals(userId)){
                    order.setUser(user);
                }
            }
        }

        Iterable<OrderGood> filteredOrders = filterByStatus((List<OrderGood>) orders, OrderGood.Status.valueOf(this.statusFilter));

        model.addAttribute("orders",OrderGood.sortByDate((List<OrderGood>) filteredOrders));
    }

    @GetMapping("/status")
    public String setStatus(@RequestParam(name = "statusFilter") String statusFilter,
                            Model model)
    {
        this.statusFilter = statusFilter;

        return "redirect:/admin/orders";
    }

    @ModelAttribute(name = "orderCompletionDate")
    public OrderCompletionDate OrderCompletionDate(){
        return new OrderCompletionDate();
    }

    @PostMapping("confirm/{id}")
    public String processOrder(@PathVariable("id") Long orderId,
                               @ModelAttribute("orderCompletionDate") OrderCompletionDate orderCompletionDate ){

        OrderGood confirmedOrder = orderService.getById(orderId);
        confirmedOrder.setOrderCompletionDate(orderCompletionDate.getDateTime());
        confirmedOrder.setStatus(OrderGood.Status.PROCESSING);
        orderService.cancelOrder(confirmedOrder);

        return "redirect:/admin/orders";
    }


    private Iterable<OrderGood> filterByStatus(List<OrderGood> orders, OrderGood.Status status) {
        return orders
                .stream()
                .filter(x -> x.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
