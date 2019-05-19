package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.dto.PaymentTransfer;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentWorkflowApi {
    @ApiOperation("Transfer payment to a bank")
    @PostMapping("v1/payment-transfers")
    @ResponseStatus(HttpStatus.CREATED)
    void transferPayment(@RequestBody PaymentTransfer paymentTransfer) { }
}
