package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.ProductCatalog;
import com.example.demo.service.RecommendationService;


@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @PostMapping
    public List<ProductCatalog> getRecommendations(@RequestBody List<Long> productIds) {
        return service.recommend(productIds);
    }
}
