package com.blautech.cart.controller;

import com.blautech.cart.entity.Cart;
import com.blautech.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam String userEmail) {
        return ResponseEntity.ok(cartService.getOrCreateCart(userEmail));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItem(@RequestParam String userEmail, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addItemToCart(userEmail, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(@RequestParam String userEmail, @RequestParam Long productId) {
        cartService.removeItemFromCart(userEmail, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam String userEmail) {
        cartService.clearCart(userEmail);
        return ResponseEntity.ok().build();
    }
}
