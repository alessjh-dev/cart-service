package com.blautech.cart.service;

import com.blautech.cart.entity.Cart;

public interface CartService {
    Cart getOrCreateCart(String userEmail);
    void addItemToCart(String userEmail, Long productId, int quantity);
    void removeItemFromCart(String userEmail, Long productId);
    void clearCart(String userEmail);
}
