package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Price;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCatalog;
import com.example.demo.model.Stock;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CatalogService {

    @Autowired
    private RestTemplate restTemplate;

    // =========================
    // GET ALL PRODUCTS
    // =========================
    @CircuitBreaker(name = "catalogservice", fallbackMethod = "fallbackList")
    public List<ProductCatalog> getAllProducts() {

        List<ProductCatalog> result = new ArrayList<>();

        List<Product> products = restTemplate.exchange(
                "http://PRODUCT/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody();

        for (Product product : products) {
            result.add(buildCatalog(product.getPid()));
        }

        return result;
    }

    // =========================
    // GET BY ID
    // =========================
    @CircuitBreaker(name = "catalogservice", fallbackMethod = "fallbackSingle")
    public ProductCatalog getById(Long id) {
        return buildCatalog(id);
    }

    // =========================
    // GET BY CATEGORY
    // =========================
    @CircuitBreaker(name = "catalogservice", fallbackMethod = "fallbackCategory")
    public List<ProductCatalog> getByCategory(String category) {

        List<ProductCatalog> result = new ArrayList<>();

        List<Product> products = restTemplate.exchange(
                "http://PRODUCT/products/category/" + category,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                }).getBody();

        for (Product product : products) {
            result.add(buildCatalog(product.getPid()));
        }

        return result;
    }

    // =========================
    // COMMON METHOD
    // =========================
    private ProductCatalog buildCatalog(Long pid) {

        Product product = restTemplate.getForObject(
                "http://PRODUCT/products/" + pid,
                Product.class);

        Price price = restTemplate.getForObject(
                "http://PRICING/prices/" + pid,
                Price.class);

        Stock stock = restTemplate.getForObject(
                "http://INVENTORY/stocks/" + pid,
                Stock.class);

        ProductCatalog catalog = new ProductCatalog();
        catalog.setPid(product.getPid());
        catalog.setPname(product.getPname());
        catalog.setPcategory(product.getPcategory());
        catalog.setDiscountedPrice(price.getDiscountedPrice());
        catalog.setNoOfItems(stock.getNoOfItemsLeft());

        return catalog;
    }

    // =========================
    // FALLBACK LIST
    // =========================
    public List<ProductCatalog> fallbackList(Exception ex) {

        System.out.println("Fallback List triggered");

        List<ProductCatalog> list = new ArrayList<>();

        ProductCatalog catalog = new ProductCatalog();
        catalog.setPid(0L);
        catalog.setPname("Service Down");
        catalog.setPcategory("N/A");
        catalog.setDiscountedPrice(0.0);
        catalog.setNoOfItems(0);

        list.add(catalog);

        return list;
    }

    // =========================
    // FALLBACK SINGLE
    // =========================
    public ProductCatalog fallbackSingle(Long id, Exception ex) {

        System.out.println("Fallback Single triggered");

        ProductCatalog catalog = new ProductCatalog();
        catalog.setPid(id);
        catalog.setPname("Service Down");
        catalog.setPcategory("N/A");
        catalog.setDiscountedPrice(0.0);
        catalog.setNoOfItems(0);

        return catalog;
    }

    // =========================
    // FALLBACK CATEGORY
    // =========================
    public List<ProductCatalog> fallbackCategory(String category, Exception ex) {

        System.out.println("Fallback Category triggered");

        List<ProductCatalog> list = new ArrayList<>();

        ProductCatalog catalog = new ProductCatalog();
        catalog.setPid(0L);
        catalog.setPname("Service Down");
        catalog.setPcategory(category);
        catalog.setDiscountedPrice(0.0);
        catalog.setNoOfItems(0);

        list.add(catalog);

        return list;
    }
}