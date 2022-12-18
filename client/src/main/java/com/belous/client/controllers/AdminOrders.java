package com.belous.client.controllers;

import com.belous.client.models.DatePeriod;
import com.belous.client.models.OrderCompletionDate;
import com.belous.client.models.OrderGood;
import com.belous.client.models.User;
import com.belous.client.services.ExportPdfService;
import com.belous.client.services.OrderService;
import com.belous.client.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@SessionAttributes("datePeriod"
)
public class AdminOrders {
    @Autowired
    private ExportPdfService exportPdfService;

    private OrderService orderService;

    private UserService userService;

    private String statusFilter = "WAITING";

    @GetMapping()
    public String showAdminOrdersPage(@CookieValue(name = "role",defaultValue = "")String role,Model model){
        model.addAttribute("role",role);
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
    public void addOrdersToModel(Model model,@CookieValue(name = "access_token",defaultValue = "") String accessToken,@ModelAttribute("datePeriod") DatePeriod datePeriod){
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

        Iterable<OrderGood> filteredOrders = filterByStatus((List<OrderGood>) orders, OrderGood.Status.valueOf(this.statusFilter),datePeriod);

        model.addAttribute("orders",OrderGood.sortByDate((List<OrderGood>) filteredOrders));
    }

    @ModelAttribute("datePeriod")
    public DatePeriod datePeriod(Model model){
        DatePeriod datePeriod = (DatePeriod) model.getAttribute("datePeriod");
        if(datePeriod==null)
        {
            datePeriod = new DatePeriod();
            datePeriod.setDateFrom(LocalDateTime.now());
            datePeriod.setDateTo(LocalDateTime.now());
        }
        return datePeriod;
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


    private Iterable<OrderGood> filterByStatus(List<OrderGood> orders, OrderGood.Status status, DatePeriod datePeriod) {
        return orders
                .stream()
                .filter(x -> x.getStatus().equals(status))
                .filter(x->x.getOrderDate().isAfter(datePeriod.getDateFrom()) && x.getOrderDate().isBefore(datePeriod.getDateTo()))
                .collect(Collectors.toList());
    }

    @PostMapping("/pdf")
    public void saveOrderAndGetPdfPaycheck(@ModelAttribute("datePeriod") DatePeriod datePeriod,
                                           @ModelAttribute("orders") Iterable<OrderGood> orders,
                                           HttpServletResponse response) throws IOException {

        BigDecimal totalReportPrice = new BigDecimal(0);
        for (OrderGood order:orders){
            totalReportPrice = totalReportPrice.add(order.totalPrice());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("orders",orders);
        data.put("datePeriod",datePeriod);
        data.put("totalReportPrice",totalReportPrice);
        ByteArrayInputStream exportedData = exportPdfService.exportReceiptPdf("/pdf-templates/salesPeriodReport",data);
        response.setHeader("Content-Disposition", "attachment; filename=salesReport.pdf");
        IOUtils.copy(exportedData, response.getOutputStream());
        response.getOutputStream().close();
        response.getOutputStream().flush();
        response.setHeader("Refresh", "1; url = index");
    }
}
