package com.kyripay.paymentworkflow.adapter.payments;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import com.kyripay.paymentworkflow.domain.port.out.Payments;
import org.springframework.stereotype.Service;

@Service
public class PaymentsImpl implements Payments {
    @Override
    public Payment getPaymentById(long id) {
        return null;
    }
}
