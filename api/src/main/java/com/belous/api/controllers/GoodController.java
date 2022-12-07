package com.belous.api.controllers;

import com.belous.api.models.Good;
import com.belous.api.repos.GoodRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/goods",produces = "application/json")
@CrossOrigin(origins = "http://localhost:5000")
public class GoodController {

    private GoodRepository goodRepository;

    public GoodController(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    @GetMapping
    public Iterable<Good> allGoods(){
        return goodRepository.findAll();
    }

    @GetMapping("/{id}")
    public Good getOne(@PathVariable("id") Long id){
        return goodRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Товара не существует"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Good saveGood(@RequestBody Good good){
        return goodRepository.save(good);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editGood(@PathVariable("id") Long id,@RequestBody Good requestGood){
        Optional<Good> good = goodRepository.findById(id);

        if (good.isPresent()){
            Good editedGood = good.get();
            editedGood.setName(requestGood.getName());
            editedGood.setCount(requestGood.getCount());
            editedGood.setPrice(requestGood.getPrice());
            goodRepository.save(editedGood);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable("id") Long goodId){
        goodRepository.deleteById(goodId);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(){
        return "Такого товара не существует";
    }
}
