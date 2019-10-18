package com.kyripay.payment.domain.port.out.payment;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.domain.SearchCriterias;

import java.util.List;
import java.util.UUID;

public interface Payments {

    Payment create(UUID userId, Payment payment);

    List<Payment> readAll(UUID userId, int limit, int offset);

    List<Payment> search(SearchCriterias searchCriterias, int limit, int offset);

    Payment readById(UUID userId, long paymentId);

    Status getStatus(UUID userId, long paymentId);

    Status updateStatus(UUID userId, long paymentId, Status status);

}
