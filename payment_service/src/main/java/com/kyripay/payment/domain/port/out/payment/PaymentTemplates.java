package com.kyripay.payment.domain.port.out.payment;

import com.kyripay.payment.domain.PaymentTemplate;

import java.util.List;
import java.util.UUID;

public interface PaymentTemplates {

    PaymentTemplate create(UUID userId, PaymentTemplate data);

    List<PaymentTemplate> readAll(UUID userId, int limit, int offset);

    PaymentTemplate readById(UUID userId, long paymentTemplateId);

    PaymentTemplate update(UUID userId, long templateId, PaymentTemplate data);

    void delete(UUID userId, long paymentTemplateId);

}
