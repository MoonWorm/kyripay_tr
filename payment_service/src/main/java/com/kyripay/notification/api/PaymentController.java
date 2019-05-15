/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import com.kyripay.notification.dto.Currency;
import com.kyripay.notification.dto.IdentifiablePaymentDetails;
import com.kyripay.notification.dto.Payment;
import com.kyripay.notification.dto.PaymentDetails;
import com.kyripay.notification.dto.PaymentStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.kyripay.notification.dto.ConnectionLine.connectionLineBuilder;
import static com.kyripay.notification.dto.PaymentDetails.paymentDetailsBuilder;
import static com.kyripay.notification.dto.RecipientInfo.recipientInfoBuilder;
import static com.kyripay.notification.dto.Status.COMPLETED;
import static com.kyripay.notification.dto.Status.PROCESSING;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Endpoint", description = "Set of API methods that are exposed for payments management")
public class PaymentController
{

  @ApiOperation("Creates a passed payment")
  @PostMapping("/api/v1/payments")
  IdentifiablePaymentDetails create(@RequestBody PaymentDetails payment)
  {
    return new IdentifiablePaymentDetails(1L, payment);
  }


  @ApiOperation("Reads an existing payment template by id")
  @GetMapping("/api/v1/payments/{id}")
  Payment readById(@PathVariable Long id)
  {
    return new Payment(
        paymentDetailsBuilder()
            .userId(2L)
            .name("Template 1")
            .currency(Currency.BYN)
            .amount(50L)
            .bankName("Foo Bank")
            .accountNumber("12345")
            .paymentFormat("ISO_123")
            .recipientInfo(recipientInfoBuilder().firstName("Vasia").lastName("Pupkin").bankName("Bar Bank")
                .bankAddress("Main Street 1-1").accountNumber("54321").build())
            .connectionLine(connectionLineBuilder().protocol("http").host("9.8.7.6").port(32).path("/pay")
                .login("foo").password("bar").build())
            .build(),
        COMPLETED
    );
  }


  @ApiOperation("Reads an existing payment template by id")
  @GetMapping("/api/v1/payments/{id}/status")
  PaymentStatus getPaymentStatus()
  {
    return new PaymentStatus(PROCESSING);
  }


  @ApiOperation("Updates a status for an existing payment")
  @PutMapping("/api/v1/payments/{id}/status")
  void updateStatus(PaymentStatus status)
  {

  }

}