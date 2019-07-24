package com.kyripay.payment.dao;

import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.domain.PaymentTemplate;

import java.util.List;

public interface PaymentTemplateRepository {

    PaymentTemplate create(long userId, PaymentTemplate data) throws RepositoryException;

    List<PaymentTemplate> readAll(long userId, int limit, int offset) throws RepositoryException;

    PaymentTemplate readById(long userId, long paymentTemplateId) throws RepositoryException;

    PaymentTemplate update(long userId, long templateId, PaymentTemplate data) throws RepositoryException;

    void delete(long userId, long paymentTemplateId) throws RepositoryException;

}
