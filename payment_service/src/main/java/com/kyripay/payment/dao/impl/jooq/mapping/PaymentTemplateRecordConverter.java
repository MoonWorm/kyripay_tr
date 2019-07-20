package com.kyripay.payment.dao.impl.jooq.mapping;

import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import org.dozer.DozerConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PaymentTemplateRecordConverter extends DozerConverter<PaymentTemplateRecord, PaymentTemplate> {

    public PaymentTemplateRecordConverter() {
        super(PaymentTemplateRecord.class, PaymentTemplate.class);
    }

    @Override
    public PaymentTemplate convertTo(PaymentTemplateRecord source, PaymentTemplate destination) {
        return PaymentTemplate.builder().id(source.getId())
                .name(source.getName())
                .createdOn(LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getCreatedOn().getTime()),
                        ZoneId.systemDefault()))
                .amount(new Amount(source.getAmount(), Currency.valueOf(source.getCurrency())))
                .bankId(source.getBankId())
                .accountNumber(source.getAccountNumber())
                .recipientInfo(
                        com.kyripay.payment.domain.RecipientInfo.builder()
                                .firstName(source.getRecipientFirstName())
                                .lastName(source.getRecipientLastName())
                                .bankName(source.getRecipientBankName())
                                .bankAddress(source.getRecipientBankAddress())
                                .accountNumber(source.getRecipientBankAccount())
                                .build()
                ).build();
    }

    @Override
    public PaymentTemplateRecord convertFrom(PaymentTemplate source, PaymentTemplateRecord destination) {
        destination.setName(source.getName());
        destination.setBankId(source.getBankId());
        destination.setAmount(source.getAmount().getAmount());
        destination.setCurrency(source.getAmount().getCurrency().name());
        destination.setAccountNumber(source.getAccountNumber());
        if (source.getRecipientInfo() != null) {
            com.kyripay.payment.domain.RecipientInfo recipientInfo = source.getRecipientInfo();
            destination.setRecipientFirstName(recipientInfo.getFirstName());
            destination.setRecipientLastName(recipientInfo.getLastName());
            destination.setRecipientBankName(recipientInfo.getBankName());
            destination.setRecipientBankAddress(recipientInfo.getBankAddress());
            destination.setRecipientBankAccount(recipientInfo.getAccountNumber());
        }
        return destination;
    }
}
