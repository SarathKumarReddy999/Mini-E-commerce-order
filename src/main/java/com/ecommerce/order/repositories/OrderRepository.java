package com.ecommerce.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.order.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
