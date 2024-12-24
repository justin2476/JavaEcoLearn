package com.boi.boi_backend.service;

import com.boi.boi_backend.model.*;
import com.boi.boi_backend.repository.CartRepository;
import com.boi.boi_backend.repository.OrderRepository;
import com.boi.boi_backend.repository.ProductRepository;
import com.boi.boi_backend.repository.UserRepository;
import com.boi.boi_backend.utils.JwtUtils;
import com.boi.boi_backend.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;

    public ResponseEntity<List<Order>> getAllOrders(){
        return  ResponseEntity.ok(orderRepository.findAll());
    }
    public ResponseEntity<List<Order>> getAllOrdersByStatus(String status){
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return  ResponseEntity.ok(orderRepository.findByOrderStatus(orderStatus));
    }
    @Transactional
    public Order placeOrder(Order order) {
        double totalPrice = 0;
        // Calculate total price of the order
        for (OrderItems item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + item.getProduct().getId()));
            totalPrice += product.getPrice() * item.getQuantity();
            // Update inventory (decrease stock)
            if (product.getStock() >= item.getQuantity()) {
//                productService.purchase(item.getProduct().getId(), item.getQuantity());
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            } else {
                throw new RuntimeException("Not enough stock for product: " + item.getProduct().getName());
            }
            item.setOrder(order);
        }

        // Set the total price after discount
        order.setTotalPrice(totalPrice);
        User user = userRepository.findById(order.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        // Save order
        order.setUser(user);
        order = orderRepository.save(order);

        return order;
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getOrderStatus()==OrderStatus.CANCELLED||order.getOrderStatus()==OrderStatus.DELIVERED){
            throw new RuntimeException("Order already cancelled");
        }
        // Update inventory (increase stock)
        for(OrderItems item : order.getOrderItems()){
            productService.purchase(item.getProduct().getId(), -item.getQuantity());
//            Product product = productRepository.findById(item.getProduct().getId())
//                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + item.getProduct().getId()));
//            product.setStock(product.getStock() + item.getQuantity());
//            productRepository.save(product);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return order;
    }
    @Transactional
    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getOrderStatus()==OrderStatus.DELIVERED||order.getOrderStatus()==OrderStatus.CANCELLED){
            throw new RuntimeException("Order already cancelled");
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        order = orderRepository.save(order);
        return order;
    }
    @Transactional
    public Order shipOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if(order.getOrderStatus()==OrderStatus.SHIPPED||order.getOrderStatus()==OrderStatus.CANCELLED
        ||order.getOrderStatus()==OrderStatus.DELIVERED){
            throw new RuntimeException("Order already shipped or cancelled");
        }
        order.setOrderStatus(OrderStatus.SHIPPED);
        order = orderRepository.save(order);
        return order;
    }
    public Order checkoutOrder(String JwtToken,Long userId){
        Optional<User> user;
        if(userId!=null) {
             user = userRepository.findById(userId);
        }
        else {
            String token = JwtToken.replace("Bearer ", "");
            String userName = jwtUtils.extractUsername(token);
            user = Optional.ofNullable(userRepository.findByUsername(userName));
        }
        Cart cart= cartRepository.findByUser(user.get()).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );
        Order order = new Order();
        order.setUser(user.get());
        order.setOrderStatus(OrderStatus.PENDING);
        List<CartItems> items = cart.getItems();

        Order finalOrder = order;
        List<OrderItems> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItems orderItem = new OrderItems();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setOrder(finalOrder);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setTotalPrice(cart.getTotalPrice());
        order = orderRepository.save(order);
        return order;
    }
}
