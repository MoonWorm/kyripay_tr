/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.service.PaymentTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Template Endpoint", description = "Set of API methods that allows to manage payment templates: " +
        "create, read, update and perform a deletion.")
public class PaymentTemplateController extends GenericController {

    private PaymentTemplateService paymentTemplateService;

    public PaymentTemplateController(PaymentTemplateService paymentTemplateService) {
        this.paymentTemplateService = paymentTemplateService;
    }

    @ApiOperation("Creates a passed payment template")
    @PostMapping("/paymenttemplates")
    PaymentTemplateResponse create(@RequestHeader UUID userId,
                                   @Valid @RequestBody PaymentTemplateRequest paymentTemplate) {
        return paymentTemplateService.create(userId, paymentTemplate);
    }


    @ApiOperation("Reads all the existing payment templates")
    @GetMapping("/paymenttemplates")
    List<PaymentTemplateResponse> readAll(@RequestHeader UUID userId,
                                          @RequestParam(defaultValue = "10") int limit,
                                          @RequestParam(defaultValue = "0") int offset) {
        return paymentTemplateService.readAll(userId, limit, offset);
    }


    @ApiOperation("Reads an existing payment template by id")
    @GetMapping("/paymenttemplates/{paymentTemplateId}")
    PaymentTemplateResponse readById(@RequestHeader UUID userId,
                                     @PathVariable long paymentTemplateId) {
        return paymentTemplateService.readById(userId, paymentTemplateId);
    }


    @ApiOperation("Updates passed payment template")
    @PutMapping("/paymenttemplates/{paymentTemplateId}")
    PaymentTemplateResponse update(@RequestHeader UUID userId,
                                   @PathVariable long paymentTemplateId,
                                   @RequestBody @Valid PaymentTemplateRequest paymentTemplate) {
        return paymentTemplateService.update(userId, paymentTemplateId, paymentTemplate);
    }


    @ApiOperation("Deletes payment template by its unique identifier")
    @DeleteMapping("/paymenttemplates/{paymentTemplateId}")
    void delete(@RequestHeader UUID userId,
                @PathVariable long paymentTemplateId) {
        paymentTemplateService.delete(userId, paymentTemplateId);
    }

}