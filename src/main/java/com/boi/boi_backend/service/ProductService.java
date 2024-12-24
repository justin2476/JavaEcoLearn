package com.boi.boi_backend.service;

import com.boi.boi_backend.model.Product;
import com.boi.boi_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<Map<String,String>> createProduct(Product product) {
        System.out.println(productRepository.findByName(product.getName()));
        // Check if user already exists
        if (productRepository.findByName(product.getName()).size()!=0) {
            return ResponseEntity.status(400).body(Map.of("error", "Product name is already in use"));
        }
        productRepository.save(product);

        return ResponseEntity.ok(Map.of("success", "Product successfully created"));
    }
    public  ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
    public ResponseEntity<Product> getProductById(int id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    public ResponseEntity<String> updateById(Product product) {
        productRepository.save(product);
        return ResponseEntity.ok("Product successfully updated");
    }
    public ResponseEntity<String> updateStock(Long id,int stock) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setStock(stock);
            productRepository.save(product);
            return ResponseEntity.ok("Product successfully updated");
        }
        return ResponseEntity.status(400).body("Product not found");
    }
    public ResponseEntity<String> purchase(Long id,int stock) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null&&product.getStock()>=stock) {
            product.setStock(product.getStock()-stock);
            productRepository.save(product);
            return ResponseEntity.ok("Product ordered successfully");
        }else{
            return ResponseEntity.status(400).body("Insufficent quantity in stock");
        }
    }

}
