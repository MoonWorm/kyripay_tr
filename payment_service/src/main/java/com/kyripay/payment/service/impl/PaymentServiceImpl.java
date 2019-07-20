package com.kyripay.payment.service.impl;

import com.kyripay.payment.dao.impl.jooq.JooqPaymentRepository;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.PaymentRequest;
import com.kyripay.payment.dto.PaymentResponse;
import com.kyripay.payment.service.PaymentService;
import com.kyripay.payment.service.exception.ServiceException;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Validated
@Service
public class PaymentServiceImpl implements PaymentService {

    private JooqPaymentRepository repository;
    private DozerBeanMapper mapper;
    private PaymentValidator validator;

    public PaymentServiceImpl(JooqPaymentRepository repository, DozerBeanMapper mapper,
                              PaymentValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public PaymentResponse create(long userId, PaymentRequest paymentRequest) throws ServiceException {
        try {
            Payment payment = mapper.map(paymentRequest, Payment.class);
            validator.validatePayment(payment);
            Payment paymentCreated = repository.create(userId, payment);
            return mapper.map(paymentCreated, PaymentResponse.class);
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment.", e);
        }
    }

    @Override
    public List<PaymentResponse> readAll(long userId, int limit, int offset) throws ServiceException {
        try {
            return repository.readAll(userId, limit, offset)
                    .stream()
                    .map(payment -> mapper.map(payment, PaymentResponse.class))
                    .collect(toList());
        } catch (Exception e) {
            throw new ServiceException("Can't read payments.", e);
        }
    }

    @Override
    public PaymentResponse readById(long userId, long paymentTemplateId) throws ServiceException {
        try {
            Payment payment = repository.readById(userId, paymentTemplateId);
            return mapper.map(payment, PaymentResponse.class);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment by its id.", e);
        }
    }

    @Override
    public Status updateStatus(long userId, long paymentTemplateId, Status status) throws ServiceException {
        try {
            validator.validatePaymentStatus(status);
            return repository.updateStatus(userId, paymentTemplateId, status);
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment.status", e);
        }
    }

    @Override
    public Status getStatus(long userId, long paymentTemplateId) throws ServiceException {
        try {
            return repository.getStatus(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment status.", e);
        }
    }

}
