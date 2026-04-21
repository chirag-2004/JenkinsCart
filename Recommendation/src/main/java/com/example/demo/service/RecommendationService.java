package com.example.demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.ProductCatalog;


@Service
public class RecommendationService {

    @Autowired RestTemplate restTemplate;

    public List<ProductCatalog> recommend(List<Long> productIds) {
        Map<Long, ProductCatalog> uniqueProducts = new LinkedHashMap<>();

        for (Long pid : productIds) {
            ProductCatalog currentProduct = restTemplate.getForObject(
                    "http://PRODUCTCATALOG/catalogs/products/" + pid,
                    ProductCatalog.class
            );

            if (currentProduct == null) {
                continue;
            }

            List<ProductCatalog> sameCategoryProducts = restTemplate.exchange(
                    "http://PRODUCTCATALOG/catalogs/products/category/" + currentProduct.getPcategory(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductCatalog>>() {
                    }
            ).getBody();

            if (sameCategoryProducts != null) {
                for (ProductCatalog product : sameCategoryProducts) {
                    if (!product.getPid().equals(pid) && !productIds.contains(product.getPid())) {
                        uniqueProducts.put(product.getPid(), product);
                    }
                }
            }
        }

        return new ArrayList<>(uniqueProducts.values());
    }
}
