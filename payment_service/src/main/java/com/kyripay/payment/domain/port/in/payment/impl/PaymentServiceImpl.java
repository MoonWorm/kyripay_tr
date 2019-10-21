package com.kyripay.payment.domain.port.in.payment.impl;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.SearchCriterias;
import com.kyripay.payment.domain.Trace;
import com.kyripay.payment.domain.port.in.payment.PaymentService;
import com.kyripay.payment.domain.port.in.payment.ServiceException;
import com.kyripay.payment.domain.port.out.payment.Payments;
import com.kyripay.payment.domain.port.out.traces.Traces;
import com.kyripay.payment.domain.vo.Status;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Service
public class PaymentServiceImpl implements PaymentService {

    private Payments repository;
    private PaymentValidator validator;

    private Traces traces;

    public PaymentServiceImpl(com.kyripay.payment.domain.port.out.payment.Payments repository,
                              PaymentValidator validator, Traces traces) {
        this.repository = repository;
        this.validator = validator;
        this.traces = traces;
    }

    @Override
    public Payment create(UUID userId, Payment payment) {
        try {
            payment.setUserId(userId);
            validator.validatePayment(payment);
            Payment paymentCreated = repository.create(userId, payment);

            Trace trace = new Trace(paymentCreated.getId());
            trace.addHeader("userId", String.valueOf(userId));
            traces.createTrace(trace);

            return paymentCreated;
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment.", e);
        }
    }

    @Override
    public List<Payment> readAll(UUID userId, int limit, int offset) {
        try {
            return repository.readAll(userId, limit, offset);
        } catch (Exception e) {
            throw new ServiceException("Can't read payments.", e);
        }
    }

    @Override
    public List<Payment> search(SearchCriterias searchCriterias, int limit, int offset) {
        try {
            return repository.search(searchCriterias, limit, offset);
        } catch (Exception e) {
            throw new ServiceException("Can't search for payments.", e);
        }
    }

    @Override
    public Payment readById(UUID userId, long paymentId) {
        try {
            return repository.readById(userId, paymentId);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment by its id.", e);
        }
    }

    @Override
    public Status updateStatus(UUID userId, long paymentId, Status status) {
        try {
            validator.validatePaymentStatus(status);
            return repository.updateStatus(userId, paymentId, status);
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment.status", e);
        }
    }

    @Override
    public Status getStatus(UUID userId, long paymentId) {
        try {
            return repository.getStatus(userId, paymentId);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment status.", e);
        }
    }

}
