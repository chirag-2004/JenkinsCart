package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ProductCatalog;
import com.example.demo.service.CatalogService;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    CatalogService service;

    @GetMapping("/products")
    public List<ProductCatalog> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductCatalog getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/products/category/{category}")
    public List<ProductCatalog> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }
}