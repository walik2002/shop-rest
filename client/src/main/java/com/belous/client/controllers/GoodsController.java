package com.belous.client.controllers;

import com.belous.client.models.Good;
import com.belous.client.services.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodService goodService;

    @GetMapping
    public String showGoodsPage(Model model){
        model.addAttribute("goods", goodService.findAll());

        return "goodsPage";
    }

    @ModelAttribute(name = "addedGood")
    public Good good(){
        return new Good();
    }

    @GetMapping("/add")
    public String showAddGoodPage(){
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
    public String showEditPage(@PathVariable("id") String goodId,Model model){
        Good editedGood = goodService.getOne(goodId);
        model.addAttribute("editedGood",editedGood);
        return "editGood";
    }
    @PostMapping("/edit/{id}")
    public String editGood(@PathVariable("id") String goodId,
                           @ModelAttribute Good editedGood){
        goodService.editGood(goodId,editedGood);
        return "redirect:/goods";
    }

}
