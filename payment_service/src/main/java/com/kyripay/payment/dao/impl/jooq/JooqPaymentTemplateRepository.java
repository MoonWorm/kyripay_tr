package com.kyripay.payment.dao.impl.jooq;

import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.dto.RecipientInfo;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import static com.kyripay.payment.dao.impl.jooq.meta.Tables.PAYMENT_TEMPLATE;

@Repository
public class JooqPaymentTemplateRepository {

    private DSLContext ctx;

    public JooqPaymentTemplateRepository(DSLContext ctx) {
        this.ctx = ctx;
    }


    public PaymentTemplateRecord create(long userId, PaymentTemplateRequest data) throws RepositoryException {
        try {
            PaymentTemplateRecord record = ctx.newRecord(PAYMENT_TEMPLATE);
            record.setUserId(userId);
            populateRecord(record, data);
            record.store();
            return record;
        } catch (Exception e) {
            throw new RepositoryException("Can't store a payment template in the repository.", e);
        }
    }

    public Result<PaymentTemplateRecord> readAll(long userId, int limit, int offset) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.USER_ID.eq(userId))
                    .orderBy(PAYMENT_TEMPLATE.CREATED_ON.asc())
                    .limit(limit)
                    .offset(offset)
                    .fetch();
        } catch (Exception e) {
            throw new RepositoryException("Can't read payment templates from the repository.", e);
        }
    }

    public PaymentTemplateRecord readById(long userId, long paymentTemplateId) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.ID.eq(paymentTemplateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .fetchAny();
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment template from the repository.", e);
        }
    }

    public PaymentTemplateRecord update(long userId, long templateId, PaymentTemplateRequest data) throws RepositoryException {
        try {
            PaymentDetails paymentDetails = data.getPaymentDetails();
            RecipientInfo recipientInfo = paymentDetails == null ? null : paymentDetails.getRecipientInfo();
            return ctx.update(PAYMENT_TEMPLATE)
                    .set(PAYMENT_TEMPLATE.BANK_ID, paymentDetails == null ? null : paymentDetails.getBankId())
                    .set(PAYMENT_TEMPLATE.AMOUNT, paymentDetails == null ? null : paymentDetails.getAmount().getAmount())
                    .set(PAYMENT_TEMPLATE.CURRENCY, paymentDetails == null ? null : paymentDetails.getAmount().getCurrency().name())
                    .set(PAYMENT_TEMPLATE.ACCOUNT_NUMBER, paymentDetails == null ? null : paymentDetails.getAccountNumber())
                    .set(PAYMENT_TEMPLATE.RECIPIENT_FIRST_NAME, recipientInfo == null ? null : recipientInfo.getFirstName())
                    .set(PAYMENT_TEMPLATE.RECIPIENT_LAST_NAME, recipientInfo == null ? null : recipientInfo.getLastName())
                    .set(PAYMENT_TEMPLATE.RECIPIENT_BANK_NAME, recipientInfo == null ? null : recipientInfo.getBankName())
                    .set(PAYMENT_TEMPLATE.RECIPIENT_BANK_ADDRESS, recipientInfo == null ? null : recipientInfo.getBankAddress())
                    .set(PAYMENT_TEMPLATE.RECIPIENT_BANK_ACCOUNT, recipientInfo == null ? null : recipientInfo.getAccountNumber())
                    .where(PAYMENT_TEMPLATE.ID.eq(templateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .returning()
                    .fetchOne();
        } catch (Exception e) {
            throw new RepositoryException("Can't update the payment template in the repository.", e);
        }
    }

    public void delete(long userId, long paymentTemplateId) throws RepositoryException {
        try {
            ctx.delete(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.ID.eq(paymentTemplateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .execute();
        } catch (Exception e) {
            throw new RepositoryException("Can't delete the payment template out from the repository.", e);
        }
    }

    private void populateRecord(PaymentTemplateRecord record, PaymentTemplateRequest data) {
        record.setName(data.getName());
        if (data.getPaymentDetails() != null) {
            PaymentDetails paymentDetails = data.getPaymentDetails();
            record.setBankId(paymentDetails.getBankId());
            record.setAmount(paymentDetails.getAmount().getAmount());
            record.setCurrency(paymentDetails.getAmount().getCurrency().name());
            record.setAccountNumber(paymentDetails.getAccountNumber());
            if (paymentDetails.getRecipientInfo() != null) {
                RecipientInfo recipientInfo = paymentDetails.getRecipientInfo();
                record.setRecipientFirstName(recipientInfo.getFirstName());
                record.setRecipientLastName(recipientInfo.getLastName());
                record.setRecipientBankName(recipientInfo.getBankName());
                record.setRecipientBankAddress(recipientInfo.getBankAddress());
                record.setRecipientBankAccount(recipientInfo.getAccountNumber());
            }
        }
    }
}