/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.dto.Currency;
import com.kyripay.payment.dto.IdentifiablePaymentDetails;
import com.kyripay.payment.dto.PaymentDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static com.kyripay.payment.dto.PaymentDetails.paymentDetailsBuilder;
import static com.kyripay.payment.dto.RecipientInfo.recipientInfoBuilder;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Payment Template Endpoint", description = "Set of API methods that allows to manage payment templates: " +
    "create, read, update and perform a deletion.")
public class PaymentTemplateController extends GenericController
{

  @ApiOperation("Creates a passed payment template")
  @PostMapping("/paymenttemplates")
  IdentifiablePaymentDetails create(@Valid @RequestBody PaymentDetails paymentTemplate)
  {
    return new IdentifiablePaymentDetails(1L, paymentTemplate);
  }


  @ApiOperation("Reads all the existing payment templates")
  @GetMapping("/paymenttemplates")
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
                .bankId(123L)
                .accountNumber("12345")
                .paymentFormat("ISO_123")
                .recipientInfo(recipientInfoBuilder().firstName("Vasia").lastName("Pupkin").bankName("Bar Bank")
                    .bankAddress("Main Street 1-1").accountNumber("54321").build())
                .build()
        ),
        new IdentifiablePaymentDetails(
            2L,
            paymentDetailsBuilder()
                .userId(3L)
                .name("Template 2")
                .currency(Currency.USD)
                .amount(50L)
                .bankId(321L)
                .accountNumber("56789")
                .paymentFormat("ISO_321")
                .recipientInfo(recipientInfoBuilder().firstName("Ivan").lastName("Ivanov").bankName("Bar Bank 2")
                    .bankAddress("Secondary Street 1-1").accountNumber("56789").build())
                .build()
        )
    );
  }


  @ApiOperation("Reads an existing payment template by id")
  @GetMapping("/paymenttemplates/{id}")
  PaymentDetails readById(@NotNull(message = "Payment template id must be specified") @PathVariable Long id)
  {
    return paymentDetailsBuilder()
        .userId(2L)
        .name("Template 1")
        .currency(Currency.BYN)
        .amount(20L)
        .bankId(123L)
        .accountNumber("12345")
        .paymentFormat("ISO_123")
        .recipientInfo(recipientInfoBuilder().firstName("Vasia").lastName("Pupkin").bankName("Bar Bank")
            .bankAddress("Main Street 1-1").accountNumber("54321").build())
        .build();
  }


  @ApiOperation("Updates passed payment template")
  @PutMapping("/paymenttemplates/{id}")
  PaymentDetails update(@NotNull(message = "Payment template id must be specified") @PathVariable Long id, @RequestBody @Valid PaymentDetails paymentTemplate)
  {
    return paymentTemplate;
  }


  @ApiOperation("Deletes payment template by its unique identifier")
  @DeleteMapping("/paymenttemplates/{id}")
  void delete(@NotNull(message = "Payment template id must be specified") @PathVariable Long id)
  {

  }

}