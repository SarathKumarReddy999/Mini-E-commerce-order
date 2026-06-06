package com.ecommerce.order.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.ecommerce.order.DTOs.ProductDTO;
import com.ecommerce.order.entities.Order;
import com.ecommerce.order.repositories.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repository;
    private final RestClient restClient;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
        // Initialize the client used to make HTTP calls to other services
        this.restClient = RestClient.create(); 
    }

    @PostMapping
    public Order placeOrder(@RequestBody Order incomingOrder) {
        
        // 1. CALL THE PRODUCT SERVICE (The Walkie-Talkie)
        String productServiceUrl = "http://localhost:9071/products/" + incomingOrder.getProductId();
        
        ProductDTO productData = restClient.get()
                .uri(productServiceUrl)
                .retrieve()
                .body(ProductDTO.class); // Convert the JSON response into our DTO

        // 2. Calculate the price (Using the new getter method!)
        double calculatedTotal = productData.getPrice() * incomingOrder.getQuantity();
        incomingOrder.setTotalPrice(calculatedTotal);

        // 3. Save to the Order Database
        return repository.save(incomingOrder);
    }

}
