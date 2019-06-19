package com.kyripay.payment.dao.impl.jooq;

import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentRecord;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentRequest;
import com.kyripay.payment.dto.RecipientInfo;
import com.kyripay.payment.dto.Status;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kyripay.payment.dao.impl.jooq.meta.Tables.PAYMENT;

@Repository
public class JooqPaymentRepository {

    private DSLContext ctx;

    public JooqPaymentRepository(DSLContext ctx) {
        this.ctx = ctx;
    }


    public PaymentRecord create(long userId, PaymentRequest data) throws RepositoryException {
        try {
            PaymentRecord record = ctx.newRecord(PAYMENT);
            record.setUserId(userId);
            populateRecord(record, data);
            record.setStatus(Status.CREATED.name());
            record.store();
            return record;
        } catch (Exception e) {
            throw new RepositoryException("Can't store a payment in the repository.", e);
        }
    }

    public List<PaymentRecord> readAll(long userId, int limit, int offset) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT)
                    .where(PAYMENT.USER_ID.eq(userId))
                    .orderBy(PAYMENT.CREATED_ON.asc())
                    .limit(limit)
                    .offset(offset)
                    .fetch();
        } catch (Exception e) {
            throw new RepositoryException("Can't read payments from the repository.", e);
        }
    }

    public PaymentRecord readById(long userId, long paymentId) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT)
                    .where(PAYMENT.ID.eq(paymentId).and(PAYMENT.USER_ID.eq(userId)))
                    .fetchAny();
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment from the repository.", e);
        }
    }

    public String getStatus(long userId, long paymentId) throws RepositoryException {
        try {
            return ctx.select(PAYMENT.STATUS).from(PAYMENT)
                    .where(PAYMENT.ID.eq(paymentId).and(PAYMENT.USER_ID.eq(userId)))
                    .fetchAny()
                    .getValue(PAYMENT.STATUS);
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment status from the repository.", e);
        }
    }

    public String updateStatus(long userId, long paymentId, String status) throws RepositoryException {
        try {
            return ctx.update(PAYMENT)
                    .set(PAYMENT.STATUS, status)
                    .where(PAYMENT.ID.eq(paymentId).and(PAYMENT.USER_ID.eq(userId)))
                    .returning(PAYMENT.STATUS)
                    .fetchOne()
                    .get(PAYMENT.STATUS);

        } catch (Exception e) {
            throw new RepositoryException("Can't update the payment status in the repository.", e);
        }
    }

    private void populateRecord(PaymentRecord record, PaymentRequest data) {
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