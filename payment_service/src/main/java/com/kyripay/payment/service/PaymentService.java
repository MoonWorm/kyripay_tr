package com.kyripay.payment.service;

import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.*;
import com.kyripay.payment.service.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentService {

    @Transactional
    PaymentResponse create(long userId,
                           PaymentRequest paymentTemplate) throws ServiceException;

    @Transactional(readOnly = true)
    List<PaymentResponse> readAll(long userId, int limit, int offset) throws ServiceException;


    @Transactional(readOnly = true)
    PaymentResponse readById(long userId, long paymentId) throws ServiceException;

    @Transactional
    Status getStatus(long userId, long paymentId) throws ServiceException;

    @Transactional
    Status updateStatus(long userId, long paymentId, Status status) throws ServiceException;

}
