package com.boi.boi_backend.repository;

import com.boi.boi_backend.model.Order;
import com.boi.boi_backend.model.Product;
import com.boi.boi_backend.utils.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(long id);
    List<Order> findByOrderStatus(OrderStatus status);
}