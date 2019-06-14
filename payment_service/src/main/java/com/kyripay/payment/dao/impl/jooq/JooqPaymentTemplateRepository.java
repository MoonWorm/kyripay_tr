package com.kyripay.payment.dao.impl.jooq;

import com.kyripay.payment.dao.PaymentTemplateRepository;
import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import static com.kyripay.payment.dao.impl.jooq.meta.Tables.PAYMENT_TEMPLATE;

@Repository
public class JooqPaymentTemplateRepository implements PaymentTemplateRepository {

    private DSLContext ctx;

    public JooqPaymentTemplateRepository(DSLContext ctx) {
        this.ctx = ctx;
    }


    @Override
    public PaymentTemplateRecord create(long userId, PaymentTemplateRequest data) throws RepositoryException {
        try {
            PaymentTemplateRecord record = ctx.newRecord(PAYMENT_TEMPLATE);
            record.setUserId(userId);
            populateRecord(record, data);
            ctx.executeInsert(record);
            return readByName(userId, record.getName());
        } catch (Exception e) {
            throw new RepositoryException("Can't store payment template in repository.", e);
        }
    }

    @Override
    public Result<PaymentTemplateRecord> readAll(long userId, int limit, int offset) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.USER_ID.eq(userId))
                    .orderBy(PAYMENT_TEMPLATE.NAME.asc())
                    .limit(limit)
                    .offset(offset)
                    .fetch();
        } catch (Exception e) {
            throw new RepositoryException("Can't read payment templates from the repository.", e);
        }
    }

    @Override
    public PaymentTemplateRecord readById(long userId, long paymentTemplateId) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.ID.eq(paymentTemplateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .fetchAny();
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment template from the repository.", e);
        }
    }

    @Override
    public PaymentTemplateRecord readByName(long userId, String name) throws RepositoryException {
        try {
            return ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.USER_ID.eq(userId).and(PAYMENT_TEMPLATE.NAME.eq(name)))
                    .fetchAny();
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment template from the repository.", e);
        }
    }

    @Override
    public PaymentTemplateRecord update(long userId, long templateId, PaymentTemplateRequest data) throws RepositoryException {
        try {
            PaymentTemplateRecord record = ctx.newRecord(PAYMENT_TEMPLATE);
            record.setId(templateId);
            record.setUserId(userId);
            populateRecord(record, data);
            ctx.executeUpdate(record);
            return record;
        } catch (Exception e) {
            throw new RepositoryException("Can't update the payment template in the repository.", e);
        }
    }

    private void populateRecord(PaymentTemplateRecord record, PaymentTemplateRequest data) {
        record.setName(data.getName());
        record.setBankId(data.getPaymentDetails().getBankId());
        record.setAmount(data.getPaymentDetails().getAmount().getAmount());
        record.setCurrency(data.getPaymentDetails().getAmount().getCurrency().name());
        record.setAccountNumber(data.getPaymentDetails().getAccountNumber());
        record.setRecipientFirstName(data.getPaymentDetails().getRecipientInfo().getFirstName());
        record.setRecipientLastName(data.getPaymentDetails().getRecipientInfo().getLastName());
        record.setRecipientBankName(data.getPaymentDetails().getRecipientInfo().getBankName());
        record.setRecipientBankAddress(data.getPaymentDetails().getRecipientInfo().getBankAddress());
        record.setRecipientBankAccount(data.getPaymentDetails().getRecipientInfo().getAccountNumber());
    }

    @Override
    public void delete(long userId, long paymentTemplateId) throws RepositoryException {
        try {
            ctx.delete(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.ID.eq(paymentTemplateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .execute();
        } catch (Exception e) {
            throw new RepositoryException("Can't delete the payment template out from the repository.", e);
        }
    }

}