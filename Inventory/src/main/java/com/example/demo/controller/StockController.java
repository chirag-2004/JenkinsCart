package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Stock;
import com.example.demo.service.StockService;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired StockService service;

    @PostMapping
    public Stock addStock(@RequestBody Stock stock) {
        return service.save(stock);
    }

    @GetMapping("/{pid}")
    public Stock getStock(@PathVariable Long pid) {
        return service.findById(pid);
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return service.findAll();
    }
}
