package com.kyripay.payment.dao.impl.jooq.mapping;

import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentRecord;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.domain.vo.Status;
import org.dozer.DozerConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PaymentRecordConverter extends DozerConverter<PaymentRecord, Payment> {

    public PaymentRecordConverter() {
        super(PaymentRecord.class, Payment.class);
    }

    @Override
    public Payment convertTo(PaymentRecord source, Payment destination) {
        if (source == null) {
            return null;
        }
        return Payment.builder().id(source.getId())
                .status(Status.valueOf(source.getStatus()))
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
                                .accountNumber(source.getAccountNumber())
                                .build()
                ).build();
    }

    @Override
    public PaymentRecord convertFrom(Payment source, PaymentRecord destination) {
        if (source == null) {
            return null;
        }
        destination.setStatus(source.getStatus().name());
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
