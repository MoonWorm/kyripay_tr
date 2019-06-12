package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.dto.PaymentTransfer;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PaymentWorkflowApi {
    @ApiOperation("Transfer payment to a bank")
    @PostMapping("/payment-transfers")
    @ResponseStatus(HttpStatus.OK)
    void transferPayment(@RequestBody PaymentTransfer paymentTransfer) { }
}
