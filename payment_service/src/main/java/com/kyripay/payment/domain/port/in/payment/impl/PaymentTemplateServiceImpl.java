package com.kyripay.payment.domain.port.in.payment.impl;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.port.in.payment.PaymentTemplateService;
import com.kyripay.payment.domain.port.in.payment.ServiceException;
import com.kyripay.payment.domain.port.out.payment.PaymentTemplates;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentTemplateServiceImpl implements PaymentTemplateService {

    private PaymentTemplates repository;
    private PaymentTemplateValidator validator;

    public PaymentTemplateServiceImpl(com.kyripay.payment.domain.port.out.payment.PaymentTemplates repository,
                                      PaymentTemplateValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public PaymentTemplate create(UUID userId, PaymentTemplate paymentTemplate) {
        try {
            validator.validatePayment(paymentTemplate);
            return repository.create(userId, paymentTemplate);
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment template.", e);
        }
    }

    @Override
    public List<PaymentTemplate> readAll(UUID userId, int limit, int offset) {
        try {
            return repository.readAll(userId, limit, offset);
        } catch (Exception e) {
            throw new ServiceException("Can't read payment templates.", e);
        }
    }

    @Override
    public PaymentTemplate readById(UUID userId, long paymentTemplateId) {
        try {
            return repository.readById(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment template.", e);
        }
    }

    @Override
    public PaymentTemplate update(UUID userId, long paymentTemplateId, PaymentTemplate paymentTemplate) {
        try {
            return repository.update(userId, paymentTemplateId, paymentTemplate);
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment template.", e);
        }
    }

    @Override
    public void delete(UUID userId, long paymentTemplateId) {
        try {
            repository.delete(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't delete the payment template.", e);
        }
    }

}
