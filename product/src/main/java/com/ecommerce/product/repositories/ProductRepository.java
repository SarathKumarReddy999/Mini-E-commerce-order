package com.ecommerce.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.product.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
