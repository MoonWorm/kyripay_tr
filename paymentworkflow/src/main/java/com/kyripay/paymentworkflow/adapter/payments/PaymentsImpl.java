package com.kyripay.paymentworkflow.adapter.payments;

import com.kyripay.paymentworkflow.api.dto.PaymentStatusUpdateRequest;
import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import com.kyripay.paymentworkflow.domain.port.out.Payments;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentsImpl implements Payments {

    PaymentsClient paymentsClient;

    public PaymentsImpl(PaymentsClient paymentsClient) {
        this.paymentsClient = paymentsClient;
    }

    @Override
    public Optional<Payment> getPaymentById(long id) {
        return paymentsClient.getPaymentById(id);
    }

    @Override
    public void updateStatus(long id, PaymentStatusUpdateRequest updateRequest) {
        paymentsClient.updateStatus(id, updateRequest);
    }
}
