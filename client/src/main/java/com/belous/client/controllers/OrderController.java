package com.belous.client.controllers;

import com.belous.client.models.OrderGood;
import com.belous.client.services.ExportPdfService;
import com.belous.client.services.OrderService;
import com.belous.client.services.RestGoodService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@SessionAttributes("currentOrder")
public class OrderController {

    @Autowired
    private ExportPdfService exportPdfService;

    private OrderService orderService;

    @GetMapping
    public String showOrdersPage(Model model,
                                 @CookieValue(name = "access_token",defaultValue = "") String accessToken,
                                 @CookieValue(name = "username") String username,
                                 @CookieValue(name = "role",defaultValue = "")String role){

        this.orderService = new OrderService(accessToken);
        Iterable<OrderGood> userOrders = orderService.getUserOrders(username);
        model.addAttribute("userOrders", OrderGood.sortByDate((List<OrderGood>) userOrders));
        model.addAttribute("role",role);
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
    @PostMapping("/pdf/{orderId}")
    public void savePdfPaycheck(@PathVariable("orderId") Long orderId, HttpServletResponse response) throws IOException {
        OrderGood order  = orderService.getById(orderId);

        Map<String,Object> data = new HashMap<>();
        data.put("currentOrder",order);
        ByteArrayInputStream exportedData = exportPdfService.exportReceiptPdf("/pdf-templates/paycheck",data);
        response.setHeader("Content-Disposition", "attachment; filename=paycheck.pdf");
        IOUtils.copy(exportedData, response.getOutputStream());
        response.getOutputStream().close();
        response.getOutputStream().flush();
        response.setHeader("Refresh", "1; url = index");
    }

}
