package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.*;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
    ProductRepository repository;
	
    public Product save(Product product) {
        return repository.save(product);
    }

    public Product findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public List<Product> findByCategory(String category) {
        return repository.findByPcategoryIgnoreCase(category);
    }
}
