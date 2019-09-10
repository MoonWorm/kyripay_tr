package com.kyripay.payment.service;

import com.kyripay.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.service.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PaymentTemplateService {

    @Transactional
    PaymentTemplateResponse create(UUID userId,
                                   PaymentTemplateRequest paymentTemplate) throws ServiceException;

    @Transactional(readOnly = true)
    List<PaymentTemplateResponse> readAll(UUID userId, int limit, int offset) throws ServiceException;


    @Transactional(readOnly = true)
    PaymentTemplateResponse readById(UUID userId, long paymentTemplateId) throws ServiceException;

    @Transactional
    PaymentTemplateResponse update(UUID userId, long paymentTemplateId, PaymentTemplateRequest data) throws ServiceException;

    @Transactional
    void delete(UUID userId, long paymentTemplateId) throws ServiceException;

}
