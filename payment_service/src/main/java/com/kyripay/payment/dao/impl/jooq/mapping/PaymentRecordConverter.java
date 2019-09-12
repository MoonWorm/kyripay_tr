package com.kyripay.payment.dao.impl.jooq.mapping;

import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentRecord;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.PaymentRecipientInfo;
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
    public Payment convertTo(PaymentRecord src, Payment dest) {
        return new Payment(
                src.getId(),
                src.getUserId(),
                new Amount(src.getAmount(), Currency.valueOf(src.getCurrency())),
                src.getBankId(),
                src.getAccountNumber(),
                Status.valueOf(src.getStatus()),
                new PaymentRecipientInfo(
                        src.getRecipientFirstName(),
                        src.getRecipientLastName(),
                        src.getRecipientBankUrn(),
                        src.getRecipientBankName(),
                        src.getRecipientBankAddress(),
                        src.getRecipientBankAccount()
                ),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(src.getCreatedOn().getTime()),
                        ZoneId.systemDefault())
        );
    }

    @Override
    public PaymentRecord convertFrom(Payment src, PaymentRecord dest) {
        dest.setUserId(src.getUserId());
        dest.setStatus(src.getStatus().name());
        dest.setBankId(src.getBankId());
        dest.setAmount(src.getAmount().getAmount());
        dest.setCurrency(src.getAmount().getCurrency().name());
        dest.setAccountNumber(src.getAccountNumber());
        PaymentRecipientInfo ri = src.getRecipientInfo();
        dest.setRecipientFirstName(ri.getFirstName());
        dest.setRecipientLastName(ri.getLastName());
        dest.setRecipientBankUrn(ri.getBankUrn());
        dest.setRecipientBankName(ri.getBankName());
        dest.setRecipientBankAddress(ri.getBankAddress());
        dest.setRecipientBankAccount(ri.getAccountNumber());
        return dest;
    }
}
