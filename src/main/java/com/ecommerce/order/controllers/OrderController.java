package com.ecommerce.order.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.ecommerce.order.DTOs.ProductDTO;
import com.ecommerce.order.entities.Order;
import com.ecommerce.order.repositories.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repository;
    private final RestClient restClient;

    public OrderController(OrderRepository repository, RestClient.Builder restClientBuilder) {
        this.repository = repository;
        // Initialize the client used to make HTTP calls to other services
        // Inject the Builder, not the raw RestClient, 
        // so that we can take advantage of the @LoadBalanced configuration!
        this.restClient = restClientBuilder.build();
    }

    @PostMapping
    @CircuitBreaker(name = "productServiceBreaker", fallbackMethod = "placeOrderFallback")
    public Order placeOrder(@RequestBody Order incomingOrder) {
        
        // 1. CALL THE PRODUCT SERVICE (The Walkie-Talkie)
        // NO MORE LOCALHOST! Use the Eureka registered name of the Product Service instead!
        String productServiceUrl = "http://product-service/products/" + incomingOrder.getProductId();
        
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

    // 2. CREATE THE FALLBACK METHOD
    // The signature must perfectly match the original method, but with an added Throwable parameter!
    public Order placeOrderFallback(Order incomingOrder, Throwable throwable) {
        
        System.out.println("Product Service is DOWN! Triggering Fallback logic.");
        
        // Return a dummy order to prevent the system from crashing, 
        // or you could throw a custom "ServiceUnavailableException" here.
        Order fallbackOrder = new Order();
        fallbackOrder.setProductId(incomingOrder.getProductId());
        fallbackOrder.setQuantity(incomingOrder.getQuantity());
        fallbackOrder.setTotalPrice(0.0); // Price is 0 because we can't fetch it!
        
        return fallbackOrder; 
    }

}
