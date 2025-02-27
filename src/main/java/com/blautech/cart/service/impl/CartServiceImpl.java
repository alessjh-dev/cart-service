package com.blautech.cart.service.impl;

import com.blautech.cart.entity.Cart;
import com.blautech.cart.entity.CartItem;
import com.blautech.cart.repository.CartRepository;
import com.blautech.cart.repository.CartItemRepository;
import com.blautech.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart getOrCreateCart(String userEmail) {
        return cartRepository.findByUserEmail(userEmail)
                .orElseGet(() -> cartRepository.save(new Cart(userEmail)));
    }

    @Override
    public void addItemToCart(String userEmail, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userEmail);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setQuantity(0); // ✅ Corregido
                    newItem.setProductId(productId);
                    newItem.setCart(cart);
                    cart.getItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItemFromCart(String userEmail, Long productId) {
        Cart cart = getOrCreateCart(userEmail);
        System.out.println("cart.toString() = " + cart.getId() + " " + cart.getUserEmail());
        Optional<CartItem> item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (item.isPresent()) {
            cart.getItems().remove(item.get());
            cartItemRepository.delete(item.get());
        }
    }

    @Override
    public void clearCart(String userEmail) {
        Cart cart = getOrCreateCart(userEmail);
        cartItemRepository.deleteAll(cart.getItems());
        cartRepository.delete(cart);
    }
}
