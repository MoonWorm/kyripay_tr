package com.kyripay.payment.domain.port.in.payment;

import com.kyripay.payment.domain.PaymentTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PaymentTemplates {

    @Transactional
    PaymentTemplate create(UUID userId, PaymentTemplate paymentTemplate) throws ServiceException;

    @Transactional(readOnly = true)
    List<PaymentTemplate> readAll(UUID userId, int limit, int offset) throws ServiceException;


    @Transactional(readOnly = true)
    PaymentTemplate readById(UUID userId, long paymentTemplateId) throws ServiceException;

    @Transactional
    PaymentTemplate update(UUID userId, long paymentTemplateId, PaymentTemplate data) throws ServiceException;

    @Transactional
    void delete(UUID userId, long paymentTemplateId) throws ServiceException;

}
