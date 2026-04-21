package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Price;
import com.example.demo.repository.PriceRepository;

@Service
public class PriceService {

    @Autowired
	PriceRepository repository;

    public Price save(Price price) {
        return repository.save(price);
    }

    public Price findById(Long pid) {
        return repository.findById(pid).orElse(null);
    }

    public List<Price> findAll() {
        return repository.findAll();
    }
}
