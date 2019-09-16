package com.kyripay.paymentworkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentWorkflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentWorkflowApplication.class, args);
    }
}
