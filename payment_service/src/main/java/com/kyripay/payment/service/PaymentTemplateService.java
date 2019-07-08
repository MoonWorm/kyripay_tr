package com.kyripay.payment.service;

import com.kyripay.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.service.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentTemplateService {

    @Transactional
    PaymentTemplateResponse create(long userId,
                                   PaymentTemplateRequest paymentTemplate) throws ServiceException;

    @Transactional(readOnly = true)
    List<PaymentTemplateResponse> readAll(long userId, int limit, int offset) throws ServiceException;


    @Transactional(readOnly = true)
    PaymentTemplateResponse readById(long userId, long paymentTemplateId) throws ServiceException;

    @Transactional
    PaymentTemplateResponse update(long userId, long paymentTemplateId, PaymentTemplateRequest data) throws ServiceException;

    @Transactional
    void delete(long userId, long paymentTemplateId) throws ServiceException;

}
