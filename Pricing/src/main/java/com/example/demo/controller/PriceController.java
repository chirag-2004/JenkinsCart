package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Price;
import com.example.demo.service.PriceService;


@RestController
@RequestMapping("/prices")
public class PriceController {

	@Autowired
    PriceService service;

    @PostMapping
    public Price addPrice(@RequestBody Price price) {
        return service.save(price);
    }

    @GetMapping("/{pid}")
    public Price getPrice(@PathVariable Long pid) {
        return service.findById(pid);
    }

    @GetMapping
    public List<Price> getAllPrices() {
        return service.findAll();
    }
}
