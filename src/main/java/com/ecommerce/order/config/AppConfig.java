package com.ecommerce.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {
    
    @Bean
    //Because the Order Service is the one making the outward call to the Product Service, 
    // we must update the RestClient to check the Eureka phonebook instead of relying on a hardcoded localhost IP.
    @LoadBalanced // Tells the RestClient to look up names in Eureka!
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
