package com.boi.boi_backend.controller;
import com.boi.boi_backend.model.Product;
import com.boi.boi_backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@RequestBody Product product) {
        return productService.createProduct(product);
    }
    @GetMapping("/getallproducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return productService.getAllProducts();
    }
    @PutMapping("/update")
    public  ResponseEntity<String> updateById(@RequestBody Product product) {
        return productService.updateById(product);
    }
    @PatchMapping("/update-stock")
    public ResponseEntity<String> updateStock(@RequestBody Product product) {
        return productService.updateStock(product.getId(),product.getStock());
    }
    @PatchMapping("/purchase")
    public ResponseEntity<String> purchase(@RequestBody Product product) {
        return productService.purchase(product.getId(),product.getStock());
    }

}
