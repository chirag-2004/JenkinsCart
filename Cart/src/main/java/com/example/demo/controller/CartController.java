package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.CartResponse;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired CartService service;

    @PostMapping
    public CartResponse addProduct(
            @RequestParam(required = false) Long cartId,
            @RequestBody Long pid) {
        return service.addProduct(cartId, pid);
    }

    @GetMapping("/{id}")
    public CartResponse getCart(@PathVariable Long id) {
        return service.getCart(id);
    }
}
