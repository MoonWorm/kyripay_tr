package com.kyripay.payment.infrastructure.adapter.out.payment;

import com.kyripay.payment.domain.port.out.payment.Payments;
import com.kyripay.payment.domain.port.out.payment.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentRecord;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.domain.SearchCriterias;
import org.dozer.DozerBeanMapper;
import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kyripay.payment.dao.impl.jooq.meta.Tables.PAYMENT;
import static java.util.stream.Collectors.toList;

@Repository
public class JooqPaymentRepository implements Payments {

    private DSLContext ctx;
    private DozerBeanMapper mapper;

    public JooqPaymentRepository(DSLContext ctx, DozerBeanMapper mapper) {
        this.ctx = ctx;
        this.mapper = mapper;
    }

    @Override
    public Payment create(UUID userId, Payment payment) {
        try {
            PaymentRecord record = ctx.newRecord(PAYMENT);
            mapper.map(payment, record);
            record.setUserId(userId);
            PaymentRecord recordCreated = ctx.insertInto(PAYMENT).set(record).returning().fetchOne();
            return mapper.map(recordCreated, Payment.class);
        } catch (Exception e) {
            throw new RepositoryException("Can't store a payment in the repository.", e);
        }
    }

    @Override
    public List<Payment> readAll(UUID userId, int limit, int offset) {
        try {
            return ctx.selectFrom(PAYMENT)
                    .where(PAYMENT.USER_ID.eq(userId))
                    .orderBy(PAYMENT.CREATED_ON.asc())
                    .limit(limit)
                    .offset(offset)
                    .fetch()
                    .stream()
                    .map(record -> mapper.map(record, Payment.class))
                    .collect(toList());
        } catch (Exception e) {
            throw new RepositoryException("Can't read payments from the repository.", e);
        }
    }

    @Override
    public List<Payment> search(SearchCriterias searchCriterias, int limit, int offset) {
        try {
            SelectWhereStep<PaymentRecord> select = ctx.selectFrom(PAYMENT);
            if (searchCriterias != null) {
                if (searchCriterias.getStatus() != null) {
                    select.where(PAYMENT.STATUS.eq(searchCriterias.getStatus().name()));
                }
            }
            return select.orderBy(PAYMENT.CREATED_ON.asc())
                    .limit(limit)
                    .offset(offset)
                    .fetch()
                    .stream()
                    .map(record -> mapper.map(record, Payment.class))
                    .collect(toList());
        } catch (Exception e) {
            throw new RepositoryException("Can't search for payments.", e);
        }
    }

    @Override
    public Payment readById(UUID userId, long paymentId) {
        try {
            PaymentRecord record = ctx.selectFrom(PAYMENT)
                    .where(PAYMENT.ID.eq(paymentId).and(PAYMENT.USER_ID.eq(userId)))
                    .fetchAny();
            if (record == null) {
                return null;
            }
            return mapper.map(record, Payment.class);
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment from the repository.", e);
        }
    }

    @Override
    public Status getStatus(UUID userId, long paymentId) {
        try {
            return Optional.ofNullable(
                    ctx.select(PAYMENT.STATUS)
                            .from(PAYMENT)
                            .where(PAYMENT.ID.eq(paymentId).and(PAYMENT.USER_ID.eq(userId)))
                            .fetchAny()
            ).map(result -> Status.valueOf(result.getValue(PAYMENT.STATUS))).orElse(null);
        } catch (Exception e) {
            throw new RepositoryException("Can't read a payment status from the repository.", e);
        }
    }

    @Override
    public Status updateStatus(UUID userId, long paymentId, Status status) {
        try {
            return Optional.ofNullable(
                    ctx.update(PAYMENT)
                            .set(PAYMENT.STATUS, status.name())
                            .where(PAYMENT.ID.eq(paymentId).and(PAYMENT.USER_ID.eq(userId)))
                            .returning(PAYMENT.STATUS)
                            .fetchOne()
            ).map(result -> Status.valueOf(result.get(PAYMENT.STATUS)))
                    .orElseThrow(() -> new RepositoryException("No payment with id " + paymentId + " for user with id"
                            + userId + " was found."));
        } catch (Exception e) {
            throw new RepositoryException("Can't update the payment status in the repository.", e);
        }
    }

}