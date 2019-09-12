package com.kyripay.payment.dao;

import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.PaymentWithUserIdResponse;
import com.kyripay.payment.dto.SearchCriterias;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository {

    Payment create(UUID userId, Payment payment) throws RepositoryException;

    List<Payment> readAll(UUID userId, int limit, int offset) throws RepositoryException;

    List<Payment> search(SearchCriterias searchCriterias, int limit, int offset) throws RepositoryException;

    Payment readById(UUID userId, long paymentId) throws RepositoryException;

    Status getStatus(UUID userId, long paymentId) throws RepositoryException;

    Status updateStatus(UUID userId, long paymentId, Status status) throws RepositoryException;

}
