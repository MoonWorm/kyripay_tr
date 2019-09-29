package com.kyripay.paymentworkflow.adapter.payments;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@FeignClient(name = "payment", url = "${payments.url}")
public interface PaymentsClient {
    @RequestMapping(method = RequestMethod.GET, value = "api/v1/payments/{paymentId}", consumes = "application/json")
    public Optional<Payment> getPaymentById(@PathVariable("paymentId") long id);
}
