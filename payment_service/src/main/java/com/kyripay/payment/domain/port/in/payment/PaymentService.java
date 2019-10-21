package com.kyripay.payment.domain.port.in.payment;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.SearchCriterias;
import com.kyripay.payment.domain.vo.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    @Transactional
    Payment create(UUID userId, Payment payment);

    @Transactional(readOnly = true)
    List<Payment> readAll(UUID userId, int limit, int offset);

    @Transactional(readOnly = true)
    List<Payment> search(SearchCriterias searchCriterias, int limit, int offset);

    @Transactional(readOnly = true)
    Payment readById(UUID userId, long paymentId);

    @Transactional(readOnly = true)
    Status getStatus(UUID userId, long paymentId);

    @Transactional
    Status updateStatus(UUID userId, long paymentId, Status status);

}
