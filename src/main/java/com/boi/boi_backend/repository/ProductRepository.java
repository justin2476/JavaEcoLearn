package com.boi.boi_backend.repository;

import com.boi.boi_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);  // Search products by name
    List<Product> findByName(String name);
    List<Product> findAll();
    Product findById(long id);
}
