package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Stock;
import com.example.demo.repository.StockRepository;

@Service
public class StockService {

    @Autowired
    StockRepository repository;

    public Stock save(Stock stock) {
        return repository.save(stock);
    }

    public Stock findById(Long pid) {
        return repository.findById(pid).orElse(null);
    }

    public List<Stock> findAll() {
        return repository.findAll();
    }
}
