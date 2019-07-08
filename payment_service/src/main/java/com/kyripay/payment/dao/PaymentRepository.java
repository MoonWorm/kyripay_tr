package com.kyripay.payment.dao;

import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Status;

import java.util.List;

public interface PaymentRepository {

    Payment create(long userId, Payment payment) throws RepositoryException;

    List<Payment> readAll(long userId, int limit, int offset) throws RepositoryException;

    Payment readById(long userId, long paymentId) throws RepositoryException;

    Status getStatus(long userId, long paymentId) throws RepositoryException;

    Status updateStatus(long userId, long paymentId, Status status) throws RepositoryException;

}
