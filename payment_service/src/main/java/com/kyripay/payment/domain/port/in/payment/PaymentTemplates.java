package com.kyripay.payment.domain.port.in.payment;

import com.kyripay.payment.domain.PaymentTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PaymentTemplates {

    @Transactional
    PaymentTemplate create(UUID userId, PaymentTemplate paymentTemplate);

    @Transactional(readOnly = true)
    List<PaymentTemplate> readAll(UUID userId, int limit, int offset);


    @Transactional(readOnly = true)
    PaymentTemplate readById(UUID userId, long paymentTemplateId);

    @Transactional
    PaymentTemplate update(UUID userId, long paymentTemplateId, PaymentTemplate data);

    @Transactional
    void delete(UUID userId, long paymentTemplateId);

}
