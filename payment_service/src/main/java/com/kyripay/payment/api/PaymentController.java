/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.dto.Currency;
import com.kyripay.payment.dto.IdentifiablePaymentDetails;
import com.kyripay.payment.dto.Payment;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.kyripay.payment.dto.PaymentDetails.paymentDetailsBuilder;
import static com.kyripay.payment.dto.RecipientInfo.recipientInfoBuilder;
import static com.kyripay.payment.dto.Status.COMPLETED;
import static com.kyripay.payment.dto.Status.PROCESSING;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Endpoint", description = "Set of API methods that are exposed for payments management")
public class PaymentController extends GenericController
{

  @ApiOperation("Creates a passed payment")
  @PostMapping("/payments")
  IdentifiablePaymentDetails create(@Valid @RequestBody PaymentDetails payment)
  {
    return new IdentifiablePaymentDetails(1L, payment);
  }


  @ApiOperation("Reads an existing payment template by id")
  @GetMapping("/payments/{id}")
  Payment readById(@NotNull(message = "Payment id must be specified") @PathVariable Long id)
  {
    return new Payment(
        paymentDetailsBuilder()
            .userId(2L)
            .name("Template 1")
            .currency(Currency.BYN)
            .amount(50L)
            .bankId(123L)
            .accountNumber("12345")
            .paymentFormat("ISO_123")
            .recipientInfo(recipientInfoBuilder().firstName("Vasia").lastName("Pupkin").bankName("Bar Bank")
                .bankAddress("Main Street 1-1").accountNumber("54321").build())
            .build(),
        COMPLETED
    );
  }


  @ApiOperation("Reads an existing payment template by id")
  @GetMapping("/payments/{id}/status")
  PaymentStatus getPaymentStatus()
  {
    PaymentStatus paymentStatus = new PaymentStatus();
    paymentStatus.setStatus(PROCESSING);
    return paymentStatus;
  }


  @ApiOperation("Updates a status for an existing payment")
  @PutMapping(value = "/payments/{id}/status")
  void updateStatus(@NotNull(message = "Payment id must be specified") @PathVariable Long id, @Valid @RequestBody PaymentStatus status)
  {

  }

}