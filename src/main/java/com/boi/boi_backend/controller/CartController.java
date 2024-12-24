package com.boi.boi_backend.controller;

import com.boi.boi_backend.DTO.CartDTO;
import com.boi.boi_backend.model.Cart;
import com.boi.boi_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @PostMapping("/add")
    public Cart addToCart(@RequestBody CartDTO cartDto){
        return cartService.addToCart(cartDto.getUserId(), cartDto.getProductId(), cartDto.getQuantity());
    }

}
