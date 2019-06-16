package com.kyripay.payment.service.impl;

import com.kyripay.payment.dao.impl.jooq.JooqPaymentRepository;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentRecord;
import com.kyripay.payment.dto.*;
import com.kyripay.payment.service.PaymentService;
import com.kyripay.payment.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PaymentServiceImpl implements PaymentService {

    private JooqPaymentRepository paymentRepository;

    public PaymentServiceImpl(JooqPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse create(long userId, PaymentRequest payment) throws ServiceException {
        try {
            PaymentRecord record = paymentRepository.create(userId, payment);
            return toPaymentResponse(record);
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment.", e);
        }
    }

    @Override
    public List<PaymentResponse> readAll(long userId, int limit, int offset) throws ServiceException {
        try {
            return paymentRepository.readAll(userId, limit, offset)
                    .stream().map(this::toPaymentResponse).collect(toList());
        } catch (Exception e) {
            throw new ServiceException("Can't read payments.", e);
        }
    }

    @Override
    public PaymentResponse readById(long userId, long paymentTemplateId) throws ServiceException {
        try {
            PaymentRecord record = paymentRepository.readById(userId, paymentTemplateId);
            return toPaymentResponse(record);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment by its id.", e);
        }
    }

    @Override
    public void updateStatus(long userId, long paymentTemplateId, Status status) throws ServiceException {
        try {
            paymentRepository.updateStatus(userId, paymentTemplateId, status.name());
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment.status", e);
        }
    }

    @Override
    public Status getStatus(long userId, long paymentTemplateId) throws ServiceException {
        try {
            return Status.valueOf(paymentRepository.getStatus(userId, paymentTemplateId));
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment status.", e);
        }
    }

    private PaymentResponse toPaymentResponse(PaymentRecord record) {
        return PaymentResponse.builder()
                .id(record.getId())
                .status(Status.valueOf(record.getStatus()))
                .paymentDetails(
                        PaymentDetails.builder()
                                .amount(new Amount(record.getAmount(), Currency.valueOf(record.getCurrency())))
                                .bankId(record.getBankId())
                                .accountNumber(record.getAccountNumber())
                                .recipientInfo(
                                        RecipientInfo.builder()
                                                .firstName(record.getRecipientFirstName())
                                                .lastName(record.getRecipientLastName())
                                                .bankName(record.getRecipientBankName())
                                                .bankAddress(record.getRecipientBankAddress())
                                                .accountNumber(record.getRecipientBankAccount())
                                                .build()
                                )
                                .build())
                .build();
    }

}
