package com.boi.boi_backend.model;

import com.boi.boi_backend.utils.CartStatus;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> items = new ArrayList<>(); ;
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus = CartStatus.ACTIVE;
    private double totalPrice;

    public void setUser(User user) {
        this.user = user;
    }
    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }
    public List<CartItems> getItems() {
        return items;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public double getTotalPrice() {
        return totalPrice;
    }

}
