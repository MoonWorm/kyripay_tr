package com.kyripay.payment.domain.port.in.payment.impl;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.port.in.payment.PaymentTemplates;
import com.kyripay.payment.domain.port.in.payment.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentTemplatesImpl implements PaymentTemplates {

    private com.kyripay.payment.domain.port.out.payment.PaymentTemplates repository;
    private PaymentTemplateValidator validator;

    public PaymentTemplatesImpl(com.kyripay.payment.domain.port.out.payment.PaymentTemplates repository,
                                PaymentTemplateValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public PaymentTemplate create(UUID userId, PaymentTemplate paymentTemplate) throws ServiceException {
        try {
            validator.validatePayment(paymentTemplate);
            return repository.create(userId, paymentTemplate);
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment template.", e);
        }
    }

    @Override
    public List<PaymentTemplate> readAll(UUID userId, int limit, int offset) throws ServiceException {
        try {
            return repository.readAll(userId, limit, offset);
        } catch (Exception e) {
            throw new ServiceException("Can't read payment templates.", e);
        }
    }

    @Override
    public PaymentTemplate readById(UUID userId, long paymentTemplateId) throws ServiceException {
        try {
            return repository.readById(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment template.", e);
        }
    }

    @Override
    public PaymentTemplate update(UUID userId, long paymentTemplateId, PaymentTemplate paymentTemplate) throws ServiceException {
        try {
            return repository.update(userId, paymentTemplateId, paymentTemplate);
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment template.", e);
        }
    }

    @Override
    public void delete(UUID userId, long paymentTemplateId) throws ServiceException {
        try {
            repository.delete(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't delete the payment template.", e);
        }
    }

}
