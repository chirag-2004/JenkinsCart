package com.example.demo.entity;
import java.util.List;
import java.util.Map;

public class CartResponse {

    private Long cartId;
    private Map<Long, ProductCatalog> products;
    private List<ProductCatalog> recommendations;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Map<Long, ProductCatalog> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, ProductCatalog> products) {
        this.products = products;
    }

    public List<ProductCatalog> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<ProductCatalog> recommendations) {
        this.recommendations = recommendations;
    }
}
