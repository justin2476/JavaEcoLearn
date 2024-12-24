package com.boi.boi_backend.service;

import com.boi.boi_backend.model.Cart;
import com.boi.boi_backend.model.CartItems;
import com.boi.boi_backend.model.Product;
import com.boi.boi_backend.model.User;
import com.boi.boi_backend.repository.CartRepository;
import com.boi.boi_backend.repository.ProductRepository;
import com.boi.boi_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartService(ProductRepository productRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElse(new Cart());

        cart.setUser(user);
        Optional<CartItems> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        if (existingCartItem.isPresent()) {
            CartItems item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(item.getQuantity() * product.getPrice());
        } else {
            CartItems newItem = new CartItems();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(quantity * product.getPrice());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
        // Recalculate total price
        double totalPrice = cart.getItems().stream()
                .mapToDouble(CartItems::getPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }
}
