/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import com.kyripay.notification.dto.Currency;
import com.kyripay.notification.dto.IdentifiablePaymentDetails;
import com.kyripay.notification.dto.PaymentDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.kyripay.notification.dto.ConnectionLine.connectionLineBuilder;
import static com.kyripay.notification.dto.PaymentDetails.paymentDetailsBuilder;
import static com.kyripay.notification.dto.RecipientInfo.recipientInfoBuilder;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Template Endpoint", description = "Set of API methods that allows to manage payment templates: " +
    "create, read, update and perform a deletion.")
public class PaymentTemplateController
{

  @ApiOperation("Creates a passed payment template")
  @PostMapping("/api/v1/paymenttemplates")
  IdentifiablePaymentDetails create(@RequestBody PaymentDetails paymentTemplate)
  {
    return new IdentifiablePaymentDetails(1L, paymentTemplate);
  }


  @ApiOperation("Reads all the existing payment templates")
  @GetMapping("/api/v1/paymenttemplates")
  List<IdentifiablePaymentDetails> readAll()
  {
    return Arrays.asList(
        new IdentifiablePaymentDetails(
            1L,
            paymentDetailsBuilder()
                .userId(2L)
                .name("Template 1")
                .currency(Currency.BYN)
                .amount(20L)
                .bankName("Foo Bank")
                .accountNumber("12345")
                .paymentFormat("ISO_123")
                .recipientInfo(recipientInfoBuilder().firstName("Vasia").lastName("Pupkin").bankName("Bar Bank")
                    .bankAddress("Main Street 1-1").accountNumber("54321").build())
                .connectionLine(connectionLineBuilder().protocol("http").host("9.8.7.6").port(32).path("/pay")
                    .login("foo").password("bar").build())
                .build()
        ),
        new IdentifiablePaymentDetails(
            2L,
            paymentDetailsBuilder()
                .userId(3L)
                .name("Template 2")
                .currency(Currency.USD)
                .amount(50L)
                .bankName("Foo Bank 2")
                .accountNumber("56789")
                .paymentFormat("ISO_321")
                .recipientInfo(recipientInfoBuilder().firstName("Ivan").lastName("Ivanov").bankName("Bar Bank 2")
                    .bankAddress("Secondary Street 1-1").accountNumber("56789").build())
                .connectionLine(connectionLineBuilder().protocol("https").host("pay-service.com").port(32)
                    .path("/payments").login("foo2").password("bar2").build())
                .build()
        )
    );
  }


  @ApiOperation("Reads an existing payment template by id")
  @GetMapping("/api/v1/paymenttemplates/{id}")
  PaymentDetails readById(@PathVariable Long id)
  {
    return paymentDetailsBuilder()
        .userId(2L)
        .name("Template 1")
        .currency(Currency.BYN)
        .amount(20L)
        .bankName("Foo Bank")
        .accountNumber("12345")
        .paymentFormat("ISO_123")
        .recipientInfo(recipientInfoBuilder().firstName("Vasia").lastName("Pupkin").bankName("Bar Bank")
            .bankAddress("Main Street 1-1").accountNumber("54321").build())
        .connectionLine(connectionLineBuilder().protocol("http").host("9.8.7.6").port(32).path("/pay")
            .login("foo").password("bar").build())
        .build();
  }


  @ApiOperation("Updates passed payment template")
  @PutMapping("/api/v1/paymenttemplates/{id}")
  PaymentDetails update(@PathVariable Long id, @RequestBody PaymentDetails paymentTemplate)
  {
    return paymentTemplate;
  }


  @ApiOperation("Deletes payment template by its unique identifier")
  @DeleteMapping("/api/v1/paymenttemplates/{id}")
  void delete(@PathVariable Long id)
  {

  }

}