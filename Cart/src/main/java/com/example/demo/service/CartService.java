package com.example.demo.service;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartResponse;
import com.example.demo.entity.ProductCatalog;
import com.example.demo.repository.CartRepository;

@Service
public class CartService {

   @Autowired CartRepository repository;
   @Autowired RestTemplate restTemplate;

    public CartResponse addProduct(Long cartId, Long pid) {
        Cart cart;

        if (cartId == null) {
            cart = new Cart();
        } else {
            cart = repository.findById(cartId).orElse(new Cart());
        }

        ProductCatalog product = restTemplate.getForObject(
                "http://PRODUCTCATALOG/catalogs/products/" + pid,
                ProductCatalog.class
        );

        if (product == null) {
            return null;
        }

        cart.getProductIds().add(pid);
        cart = repository.save(cart);

        return getCart(cart.getCartId());
    }

    public CartResponse getCart(Long id) {
        Cart cart = repository.findById(id).orElse(null);

        if (cart == null) {
            return null;
        }

        Map<Long, ProductCatalog> productMap = new LinkedHashMap<>();

        for (Long pid : cart.getProductIds()) {
            ProductCatalog product = restTemplate.getForObject(
                    "http://PRODUCTCATALOG/catalogs/products/" + pid,
                    ProductCatalog.class
            );
            if (product != null) {
                productMap.put(pid, product);
            }
        }

        List<ProductCatalog> recommendations = restTemplate.exchange(
                "http://RECOMMENDATION/recommendations",
                HttpMethod.POST,
                new HttpEntity<>(cart.getProductIds()),
                new ParameterizedTypeReference<List<ProductCatalog>>() {
                }
        ).getBody();

        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setProducts(productMap);
        response.setRecommendations(recommendations);

        return response;
    }
}
