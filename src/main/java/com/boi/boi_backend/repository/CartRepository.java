package com.boi.boi_backend.repository;


import com.boi.boi_backend.model.Cart;
import com.boi.boi_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findById(long id);
    Optional<Cart> findByUser(User user);

}
