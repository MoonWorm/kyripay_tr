/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.infrastructure.adapter.in.payment;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.domain.port.in.payment.PaymentTemplateService;
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
@Api(value = "Payment Template Endpoint")
public class PaymentTemplateController extends GenericController {

    private PaymentTemplateService paymentTemplateService;
    private DozerBeanMapper mapper;

    public PaymentTemplateController(PaymentTemplateService paymentTemplateService, DozerBeanMapper mapper) {
        this.paymentTemplateService = paymentTemplateService;
        this.mapper = mapper;
    }

    @ApiOperation("Creates a passed payment template")
    @PostMapping("/paymenttemplates")
    public PaymentTemplateResponse create(@RequestHeader UUID userId,
                                   @Valid @RequestBody PaymentTemplateRequest request) {
        return mapper.map(paymentTemplateService.create(userId, mapper.map(request, PaymentTemplate.class)),
                PaymentTemplateResponse.class);
    }


    @ApiOperation("Reads all the existing payment templates")
    @GetMapping("/paymenttemplates")
    public List<PaymentTemplateResponse> readAll(@RequestHeader UUID userId,
                                          @RequestParam(defaultValue = "10") int limit,
                                          @RequestParam(defaultValue = "0") int offset) {
        return paymentTemplateService.readAll(userId, limit, offset)
                .stream()
                .map(paymentTemplate -> mapper.map(paymentTemplate, PaymentTemplateResponse.class))
                .collect(toList());
    }


    @ApiOperation("Reads an existing payment template by id")
    @GetMapping("/paymenttemplates/{paymentTemplateId}")
    public PaymentTemplateResponse readById(@RequestHeader UUID userId,
                                     @PathVariable long paymentTemplateId) {
        return mapper.map(paymentTemplateService.readById(userId, paymentTemplateId), PaymentTemplateResponse.class);
    }


    @ApiOperation("Updates passed payment template")
    @PutMapping("/paymenttemplates/{paymentTemplateId}")
    public PaymentTemplateResponse update(@RequestHeader UUID userId,
                                   @PathVariable long paymentTemplateId,
                                   @RequestBody @Valid PaymentTemplateRequest request) {
        return mapper.map(paymentTemplateService.update(userId, paymentTemplateId,
                mapper.map(request, PaymentTemplate.class)), PaymentTemplateResponse.class);
    }


    @ApiOperation("Deletes payment template by its unique identifier")
    @DeleteMapping("/paymenttemplates/{paymentTemplateId}")
    public void delete(@RequestHeader UUID userId,
                @PathVariable long paymentTemplateId) {
        paymentTemplateService.delete(userId, paymentTemplateId);
    }

}