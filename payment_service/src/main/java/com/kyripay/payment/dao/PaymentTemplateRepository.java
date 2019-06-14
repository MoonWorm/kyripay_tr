package com.kyripay.payment.dao;

import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import org.jooq.Result;

public interface PaymentTemplateRepository {

    PaymentTemplateRecord create(long userId, PaymentTemplateRequest paymentTemplate) throws RepositoryException;

    Result<PaymentTemplateRecord> readAll(long userId, int limit, int offset) throws RepositoryException;

    PaymentTemplateRecord readById(long userId, long paymentTemplateId) throws RepositoryException;

    PaymentTemplateRecord readByName(long userId, String name) throws RepositoryException;

    PaymentTemplateRecord update(long userId, long templateId, PaymentTemplateRequest data) throws RepositoryException;

    void delete(long userId, long paymentTemplateId) throws RepositoryException;

}
