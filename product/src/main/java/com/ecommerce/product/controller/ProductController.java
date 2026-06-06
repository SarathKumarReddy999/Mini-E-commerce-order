package com.ecommerce.product.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.entities.Product;
import com.ecommerce.product.repositories.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    // Injecting the repository
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    // When someone goes to GET /products, run this method
    @GetMapping
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // When someone sends data to POST /products, run this method
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return repository.save(product);
    }
}
