package com.kyripay.payment.service;

import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.*;
import com.kyripay.payment.service.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    @Transactional
    PaymentResponse create(UUID userId,
                           PaymentRequest paymentTemplate) throws ServiceException;

    @Transactional(readOnly = true)
    List<PaymentResponse> readAll(UUID userId, int limit, int offset) throws ServiceException;

    @Transactional(readOnly = true)
    List<PaymentWithUserIdResponse> search(SearchCriterias searchCriterias, int limit, int offset);

    @Transactional(readOnly = true)
    PaymentResponse readById(UUID userId, long paymentId) throws ServiceException;

    @Transactional(readOnly = true)
    Status getStatus(UUID userId, long paymentId) throws ServiceException;

    @Transactional
    Status updateStatus(UUID userId, long paymentId, Status status) throws ServiceException;

}
