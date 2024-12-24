package com.boi.boi_backend.controller;

import com.boi.boi_backend.model.Order;
import com.boi.boi_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders( ) {
        return orderService.getAllOrders();
    }
    @GetMapping("/all/{status}")
    public ResponseEntity<List<Order>> getAllOrdersByStatus(@PathVariable String status ) {
        return orderService.getAllOrdersByStatus(status);
    }
    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order ) {
        return orderService.placeOrder(order);
    }
    @PostMapping("/checkout")
    public Order checkoutOrder(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(required = false) Long userId) {
        return orderService.checkoutOrder(authorizationHeader,userId);
    }

    @PutMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id) {
        // Implement order cancellation logic here
        return orderService.cancelOrder(id);
    }
    @PutMapping("/{id}/deliver")
    public Order deliverOrder(@PathVariable Long id) {
        // Implement order cancellation logic here
        return orderService.deliverOrder(id);
    }


    @PutMapping("/{id}/ship")
    public Order shipOrder(@PathVariable Long id) {
        // Implement order shipping logic here
        return orderService.shipOrder(id);
    }
}
