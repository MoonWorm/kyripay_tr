package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.dto.PaymentTransfer;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class PaymentWorkflowApi {
    @ApiOperation("Transfer payment to a bank")
    @PostMapping("v1/payment-transfers")
    @ResponseStatus(HttpStatus.CREATED)
    void transferPayment(@RequestBody PaymentTransfer paymentTransfer) { }
}
