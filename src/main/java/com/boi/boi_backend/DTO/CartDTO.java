package com.boi.boi_backend.DTO;

public class CartDTO {
    private int cartId;
    private Long userId;
    private Long productId;
    private int quantity;
    public int getCartId() {
        return cartId;
    }
    public Long getUserId() {
        return userId;
    }
    public Long getProductId() {
        return productId;
    }
    public int getQuantity() {
        return quantity;
    }

}
