package com.kyripay.payment.infrastructure.adapter.out.payment;

import com.kyripay.payment.domain.port.out.payment.PaymentTemplates;
import com.kyripay.payment.domain.port.out.payment.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.domain.PaymentTemplate;
import org.dozer.DozerBeanMapper;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kyripay.payment.dao.impl.jooq.meta.Tables.PAYMENT_TEMPLATE;
import static java.util.stream.Collectors.toList;

@Repository
public class JooqPaymentTemplateRepository implements PaymentTemplates {

    private DSLContext ctx;
    private DozerBeanMapper mapper;

    public JooqPaymentTemplateRepository(DSLContext ctx, DozerBeanMapper mapper) {
        this.ctx = ctx;
        this.mapper = mapper;
    }

    @Override
    public PaymentTemplate create(UUID userId, PaymentTemplate data) {
        try {
            PaymentTemplateRecord record = ctx.newRecord(PAYMENT_TEMPLATE);
            mapper.map(data, record);
            record.setUserId(userId);
            PaymentTemplateRecord recordCreated = ctx.insertInto(PAYMENT_TEMPLATE).set(record).returning().fetchOne();
            return mapper.map(recordCreated, PaymentTemplate.class);
        } catch (Exception e) {
            throw new RepositoryException("Can't store a payment template in the repository.", e);
        }
    }

    @Override
    public List<PaymentTemplate> readAll(UUID userId, int limit, int offset) {
        try {
            return ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.USER_ID.eq(userId))
                    .orderBy(PAYMENT_TEMPLATE.CREATED_ON.asc())
                    .limit(limit)
                    .offset(offset)
                    .fetch()
                    .stream()
                    .map(record -> mapper.map(record, PaymentTemplate.class))
                    .collect(toList());
        } catch (Exception e) {
            throw new RepositoryException("Can't read payment templates from the repository.", e);
        }
    }

    @Override
    public PaymentTemplate readById(UUID userId, long paymentTemplateId) {
        try {
            PaymentTemplateRecord record = ctx.selectFrom(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.ID.eq(paymentTemplateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .fetchAny();
            if (record == null) {
                return null;
            }
            return mapper.map(record, PaymentTemplate.class);
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment template from the repository.", e);
        }
    }

    @Override
    public PaymentTemplate update(UUID userId, long templateId, PaymentTemplate data) {
        try {
            PaymentTemplateRecord paymentTemplateRecord = ctx.newRecord(PAYMENT_TEMPLATE);
            mapper.map(data, paymentTemplateRecord);
            paymentTemplateRecord.setId(templateId);
            paymentTemplateRecord.setUserId(userId);
            return Optional.ofNullable(ctx.update(PAYMENT_TEMPLATE)
                    .set(paymentTemplateRecord)
                    .where(PAYMENT_TEMPLATE.ID.eq(templateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .returning()
                    .fetchOne()
            ).map(pt -> mapper.map(pt, PaymentTemplate.class))
                    .orElseThrow(() -> new RepositoryException("No payment template with id " + templateId + " for user " +
                            "with id" + userId + " was found."));
        } catch (Exception e) {
            throw new RepositoryException("Can't update the payment template in the repository.", e);
        }
    }

    @Override
    public void delete(UUID userId, long paymentTemplateId) {
        try {
            ctx.delete(PAYMENT_TEMPLATE)
                    .where(PAYMENT_TEMPLATE.ID.eq(paymentTemplateId).and(PAYMENT_TEMPLATE.USER_ID.eq(userId)))
                    .execute();
        } catch (Exception e) {
            throw new RepositoryException("Can't delete the payment template out from the repository.", e);
        }
    }

}