package com.kyripay.payment.service.impl;

import com.kyripay.payment.dao.PaymentTemplateRepository;
import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.service.PaymentTemplateService;
import com.kyripay.payment.service.exception.ServiceException;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PaymentTemplateServiceImpl implements PaymentTemplateService {

    private PaymentTemplateRepository repository;
    private DozerBeanMapper mapper;
    private PaymentTemplateValidator validator;

    public PaymentTemplateServiceImpl(PaymentTemplateRepository repository,
                                      DozerBeanMapper mapper,
                                      PaymentTemplateValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public PaymentTemplateResponse create(long userId, PaymentTemplateRequest paymentTemplateRequest) throws ServiceException {
        try {
            PaymentTemplate paymentTemplate = mapper.map(paymentTemplateRequest, PaymentTemplate.class);
            validator.validatePayment(paymentTemplate);
            PaymentTemplate paymentTemplateCreated = repository.create(userId, paymentTemplate);
            return mapper.map(paymentTemplateCreated, PaymentTemplateResponse.class);
        } catch (Exception e) {
            throw new ServiceException("Can't create a new payment template.", e);
        }
    }

    @Override
    public List<PaymentTemplateResponse> readAll(long userId, int limit, int offset) throws ServiceException {
        try {
            return repository.readAll(userId, limit, offset)
                    .stream()
                    .map(paymentTemplate -> mapper.map(paymentTemplate, PaymentTemplateResponse.class))
                    .collect(toList());
        } catch (Exception e) {
            throw new ServiceException("Can't read payment templates.", e);
        }
    }

    @Override
    public PaymentTemplateResponse readById(long userId, long paymentTemplateId) throws ServiceException {
        try {
            PaymentTemplate paymentTemplate = repository.readById(userId, paymentTemplateId);
            return mapper.map(paymentTemplate, PaymentTemplateResponse.class);
        } catch (Exception e) {
            throw new ServiceException("Can't read the payment template.", e);
        }
    }

    @Override
    public PaymentTemplateResponse update(long userId, long paymentTemplateId, PaymentTemplateRequest data) throws ServiceException {
        try {
            PaymentTemplate paymentTemplate = mapper.map(data, PaymentTemplate.class);
            PaymentTemplate paymentTemplateUpdated = repository.update(userId, paymentTemplateId, paymentTemplate);
            return mapper.map(paymentTemplateUpdated, PaymentTemplateResponse.class);
        } catch (Exception e) {
            throw new ServiceException("Can't update the payment template.", e);
        }
    }

    @Override
    public void delete(long userId, long paymentTemplateId) throws ServiceException {
        try {
            repository.delete(userId, paymentTemplateId);
        } catch (Exception e) {
            throw new ServiceException("Can't delete the payment template.", e);
        }
    }

}
