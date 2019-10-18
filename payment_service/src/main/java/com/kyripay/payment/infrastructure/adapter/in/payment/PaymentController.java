/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.infrastructure.adapter.in.payment;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRequest;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentResponse;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentStatus;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentWithUserIdResponse;
import com.kyripay.payment.domain.SearchCriterias;
import com.kyripay.payment.domain.port.in.payment.Payments;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.DozerBeanMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Endpoint")
public class PaymentController extends GenericController {

    private final Payments paymentService;
    private final DozerBeanMapper mapper;

    public PaymentController(Payments paymentService, DozerBeanMapper mapper) {
        this.paymentService = paymentService;
        this.mapper = mapper;
    }

    @ApiOperation("Creates a passed payment")
    @PostMapping("/payments")
    public PaymentResponse create(@RequestHeader UUID userId,
                           @Valid @RequestBody PaymentRequest request) {
        return mapper.map(paymentService.create(userId, mapper.map(request, Payment.class)), PaymentResponse.class);
    }


    @ApiOperation("Reads all existing payments")
    @GetMapping("/payments")
    public List<PaymentResponse> readAll(@RequestHeader UUID userId,
                                  @RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(defaultValue = "0") int offset) {
        return paymentService.readAll(userId, limit, offset)
                .stream()
                .map(payment -> mapper.map(payment, PaymentResponse.class))
                .collect(toList());
    }

    @ApiOperation("Search payments that are relevant to passed filter criterias")
    @GetMapping("/payments/search/result")
    public List<PaymentWithUserIdResponse> search(SearchCriterias searchCriterias,
                                           @RequestParam(defaultValue = "10") int limit,
                                           @RequestParam(defaultValue = "0") int offset) {
        return paymentService.search(searchCriterias, limit, offset)
                .stream()
                .map(payment -> mapper.map(payment, PaymentWithUserIdResponse.class))
                .collect(toList());
    }

    @ApiOperation("Reads an existing payment template by id")
    @GetMapping("/payments/{paymentId}")
    public PaymentResponse readById(@RequestHeader UUID userId,
                             @PathVariable long paymentId) {
        return mapper.map(paymentService.readById(userId, paymentId), PaymentResponse.class);
    }


    @ApiOperation("Reads an existing payment template by id")
    @GetMapping("/payments/{paymentId}/status")
    public PaymentStatus getPaymentStatus(@RequestHeader UUID userId,
                                   @PathVariable long paymentId) {
        return new PaymentStatus(paymentService.getStatus(userId, paymentId));
    }


    @ApiOperation("Updates a status for an existing payment")
    @PutMapping(value = "/payments/{paymentId}/status")
    public PaymentStatus updateStatus(@RequestHeader UUID userId,
                      @PathVariable long paymentId,
                      @Valid @RequestBody PaymentStatus status) {
        return new PaymentStatus(paymentService.updateStatus(userId, paymentId, status.getStatus()));
    }


}