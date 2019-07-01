package com.kyripay.eurekaserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

    @Value("${payment.uri}")
    String paymentUri;

    @Value("${payment.path}")
    String paymentPath;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("payments-service-route", r -> r.path(paymentPath)
                        .filters(f -> f.addResponseHeader("X-TestHeader", "foobar"))
                        .uri(paymentUri))
        .build();
    }

}
