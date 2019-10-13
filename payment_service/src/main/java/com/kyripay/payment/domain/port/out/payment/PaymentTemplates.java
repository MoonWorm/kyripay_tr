package com.kyripay.payment.domain.port.out.payment;

import com.kyripay.payment.domain.PaymentTemplate;

import java.util.List;
import java.util.UUID;

public interface PaymentTemplates {

    PaymentTemplate create(UUID userId, PaymentTemplate data) throws RepositoryException;

    List<PaymentTemplate> readAll(UUID userId, int limit, int offset) throws RepositoryException;

    PaymentTemplate readById(UUID userId, long paymentTemplateId) throws RepositoryException;

    PaymentTemplate update(UUID userId, long templateId, PaymentTemplate data) throws RepositoryException;

    void delete(UUID userId, long paymentTemplateId) throws RepositoryException;

}
