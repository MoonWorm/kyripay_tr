package com.kyripay.payment.service.impl;

import com.kyripay.payment.service.exception.ServiceException;
import com.kyripay.payment.dao.PaymentTemplateRepository;
import com.kyripay.payment.dao.impl.jooq.meta.tables.records.PaymentTemplateRecord;
import com.kyripay.payment.dto.*;
import com.kyripay.payment.service.PaymentTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PaymentTemplateServiceImpl implements PaymentTemplateService {

    private PaymentTemplateRepository paymentTemplateRepository;

    public PaymentTemplateServiceImpl(PaymentTemplateRepository paymentTemplateRepository) {
        this.paymentTemplateRepository = paymentTemplateRepository;
    }

    @Override
    public PaymentTemplateResponse create(long userId, PaymentTemplateRequest paymentTemplate) throws ServiceException {
        try {
            PaymentTemplateRecord record = paymentTemplateRepository.create(userId, paymentTemplate);
            return toPaymentTemplateResponse(record);
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment template.", e);
        }
    }

    @Override
    public List<PaymentTemplateResponse> readAll(long userId, int limit, int offset) throws ServiceException {
        try {
            return paymentTemplateRepository.readAll(userId, limit, offset)
                    .stream().map(this::toPaymentTemplateResponse).collect(toList());
        } catch (Exception e) {
            throw new ServiceException("Can't read payment templates.", e);
        }
    }

    @Override
    public PaymentTemplateResponse readById(long userId, long paymentTemplateId) throws ServiceException {
        try {
            PaymentTemplateRecord record = paymentTemplateRepository.readById(userId, paymentTemplateId);
            return toPaymentTemplateResponse(record);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment template.", e);
        }
    }

    @Override
    public PaymentTemplateResponse update(long userId, long paymentTemplateId, PaymentTemplateRequest data) throws ServiceException {
        try {
            PaymentTemplateRecord recordUpdated = paymentTemplateRepository.update(userId, paymentTemplateId, data);
            return toPaymentTemplateResponse(recordUpdated);
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment template.", e);
        }
    }

    @Override
    public void delete(long userId, long paymentTemplateId) throws ServiceException {
        try {
            paymentTemplateRepository.delete(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't delete the payment template.", e);
        }
    }

    private PaymentTemplateResponse toPaymentTemplateResponse(PaymentTemplateRecord record) {
        return PaymentTemplateResponse.builder()
                .id(record.getId())
                .name(record.getName())
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
