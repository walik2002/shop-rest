package com.belous.api.controllers;


import com.belous.api.models.OrderGood;
import com.belous.api.models.UserEntity;
import com.belous.api.repos.OrderGoodRepository;
import com.belous.api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/orders",produces = "application/json")
@CrossOrigin(origins = "http://localhost:5000")
public class OrderController {
    private final OrderGoodRepository orderGoodRepository;
    private final UserService userService;

    public OrderController(OrderGoodRepository orderGoodRepository, UserService userService) {
        this.orderGoodRepository = orderGoodRepository;
        this.userService = userService;
    }

    @GetMapping
    private Iterable<OrderGood> findAll(){
        return orderGoodRepository.findAll();
    }
    @GetMapping("/{username}")
    private Iterable<OrderGood> findUserOrders(@PathVariable("username") String username){
        UserEntity user = userService.findByUsername(username);

        return orderGoodRepository.findOrderGoodsByUser(user);
    }
    @PostMapping("/{username}")
    private ResponseEntity<OrderGood> saveUserOrder(@PathVariable("username") String username, @RequestBody OrderGood orderGood){
        UserEntity user = userService.findByUsername(username);

        OrderGood newOrderGood = orderGood;
        newOrderGood.setUserId(user.getId());



        return ResponseEntity.ok(orderGoodRepository.save(newOrderGood));
    }
    @GetMapping("/order/{orderId}")
    private OrderGood findOrderById(@PathVariable("orderId") Long orderId){
        return orderGoodRepository.findById(orderId).orElseThrow();
    }

    @PostMapping("/order/cancel")
    private String cancelOrder(@RequestBody OrderGood orderGood){
        orderGoodRepository.save(orderGood);
        return "Заказ отменен";
    }

}
