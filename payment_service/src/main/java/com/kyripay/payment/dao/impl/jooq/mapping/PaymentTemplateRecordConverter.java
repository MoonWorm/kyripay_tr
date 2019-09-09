package com.kyripay.payment.dao.impl.jooq.mapping;

import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.PaymentTemplateRecipientInfo;
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
    public PaymentTemplate convertTo(PaymentTemplateRecord src, PaymentTemplate dest) {
        return new PaymentTemplate(
                src.getId(),
                src.getName(),
                new Amount(src.getAmount(), Currency.valueOf(src.getCurrency())),
                src.getBankId(),
                src.getAccountNumber(),
                new PaymentTemplateRecipientInfo(
                        src.getRecipientFirstName(),
                        src.getRecipientLastName(),
                        src.getRecipientBankName(),
                        src.getRecipientBankAddress(),
                        src.getRecipientBankAccount()
                ),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(src.getCreatedOn().getTime()),
                        ZoneId.systemDefault())
        );
    }

    @Override
    public PaymentTemplateRecord convertFrom(PaymentTemplate src, PaymentTemplateRecord dest) {
        dest.setName(src.getName());
        dest.setBankId(src.getBankId());
        dest.setAmount(src.getAmount().getAmount());
        dest.setCurrency(src.getAmount().getCurrency().name());
        dest.setAccountNumber(src.getAccountNumber());
        if (src.getRecipientInfo() != null) {
            PaymentTemplateRecipientInfo recipientInfo = src.getRecipientInfo();
            dest.setRecipientFirstName(recipientInfo.getFirstName());
            dest.setRecipientLastName(recipientInfo.getLastName());
            dest.setRecipientBankName(recipientInfo.getBankName());
            dest.setRecipientBankAddress(recipientInfo.getBankAddress());
            dest.setRecipientBankAccount(recipientInfo.getAccountNumber());
        }
        return dest;
    }
}
