/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.dto.PaymentRequest;
import com.kyripay.payment.dto.PaymentResponse;
import com.kyripay.payment.dto.Status;
import com.kyripay.payment.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Endpoint", description = "Set of API methods that are exposed for payments management")
public class PaymentController extends GenericController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ApiOperation("Creates a passed payment")
    @PostMapping("/payments")
    PaymentResponse create(@RequestHeader long userId,
                           @Valid @RequestBody PaymentRequest payment) {
        return paymentService.create(userId, payment);
    }


    @ApiOperation("Reads all existing payments")
    @GetMapping("/payments")
    List<PaymentResponse> readAll(@RequestHeader long userId,
                                  @RequestParam int limit,
                                  @RequestParam int offset) {
        return paymentService.readAll(userId, limit, offset);
    }

    @ApiOperation("Reads an existing payment template by id")
    @GetMapping("/payments/{paymentId}")
    PaymentResponse readById(@RequestHeader long userId,
                             @PathVariable long paymentId) {
        return paymentService.readById(userId, paymentId);
    }


    @ApiOperation("Reads an existing payment template by id")
    @GetMapping("/payments/{paymentId}/status")
    PaymentStatus getPaymentStatus(@RequestHeader long userId,
                                   @PathVariable long paymentId) {
        return new PaymentStatus(paymentService.getStatus(userId, paymentId));
    }


    @ApiOperation("Updates a status for an existing payment")
    @PutMapping(value = "/payments/{paymentId}/status")
    PaymentStatus updateStatus(@RequestHeader long userId,
                      @PathVariable long paymentId,
                      @Valid @RequestBody PaymentStatus status) {
        return new PaymentStatus(paymentService.updateStatus(userId, paymentId, status.getStatus()));
    }


    /**
     * @author M-ATA
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentStatus {

        @NotNull(message = "Payment status must be specified")
        @ApiModelProperty(value = "Payment status", example = "COMPLETED")
        private Status status;

    }

}