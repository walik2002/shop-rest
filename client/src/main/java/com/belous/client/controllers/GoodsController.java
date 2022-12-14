package com.belous.client.controllers;

import com.belous.client.models.Good;
import com.belous.client.models.OrderGood;
import com.belous.client.models.User;
import com.belous.client.services.GoodService;
import com.belous.client.services.LoginService;
import com.belous.client.services.RestGoodService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
@SessionAttributes("currentOrder")
public class GoodsController {

    private GoodService goodService;

    @GetMapping
    public String showGoodsPage(Model model,@CookieValue(name = "access_token",defaultValue = "") String accessToken,
                                @CookieValue(name = "role",defaultValue = "user") String role){
        this.goodService = new RestGoodService(accessToken);
        model.addAttribute("goods", goodService.findAll());
        model.addAttribute("role",role);
        return "goodsPage";
    }
    @ModelAttribute(name = "addedGood")
    public Good good(){
        return new Good();
    }

    @GetMapping("/add")
    public String showAddGoodPage(@CookieValue(name = "access_token",defaultValue = "") String accessToken,
                                  @CookieValue(name = "role",defaultValue = "")String role, Model model){
        model.addAttribute("role",role);
        this.goodService = new RestGoodService(accessToken);
        return "addGood";
    }

    @PostMapping("/add")
    public String addGood(@ModelAttribute Good addedGood){
        Good good = new Good(addedGood.getName(), addedGood.getCount(), addedGood.getPrice());
        goodService.addGood(good);
        return "redirect:/goods";
    }
    @PostMapping("/delete/{id}")
    public String deleteGood(@PathVariable("id") String id){
        goodService.deleteGood(id);

        return "redirect:/goods";
    }
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable("id") String goodId,Model model,
                               @CookieValue(name = "role",defaultValue = "")String role){
        Good editedGood = goodService.getOne(goodId);
        model.addAttribute("editedGood",editedGood);
        model.addAttribute("role",role);
        return "editGood";
    }
    @PostMapping("/edit/{id}")
    public String editGood(@PathVariable("id") String goodId,
                           @ModelAttribute Good editedGood){
        goodService.editGood(goodId,editedGood);
        return "redirect:/goods";
    }

    @ModelAttribute(name = "currentOrder")
    public OrderGood currentOrder(Model model){
        OrderGood currentOrder;
        if(!model.containsAttribute("currentOrder")){
            currentOrder = new OrderGood();
        }else {
            currentOrder = (OrderGood) model.getAttribute("currentOrder");
        }
        return currentOrder;
    }

    @PostMapping("/order/{goodId}")
    public String addGoodToOrder(@PathVariable("goodId") Long goodId,Model model,@ModelAttribute(name = "currentOrder") OrderGood currentOrder){
        currentOrder.addGood(goodService.getOne(goodId.toString()));
        //model.addAttribute("currentOrder",currentOrder);
        return "redirect:/goods";
    }

    @PostMapping("/removeGood/{goodId}")
    public String removeGoodFromCart(@PathVariable("goodId") Long goodId,@ModelAttribute(name = "currentOrder") OrderGood currentOrder){
        List<Good> copyGoods = new ArrayList<>();
        for (Good good : currentOrder.getGoods())
            copyGoods.add(good);

        for (Good good : copyGoods){
            if(goodId.equals(good.getGoodId()))
                currentOrder.getGoods().remove(good);
        }
        if(currentOrder.getGoods().size() == 0)
            currentOrder.setGoods(null);
        return "redirect:/goods";
    }
}
