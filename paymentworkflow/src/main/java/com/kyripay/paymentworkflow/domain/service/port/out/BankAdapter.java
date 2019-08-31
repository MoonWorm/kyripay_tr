package com.kyripay.paymentworkflow.domain.service.port.out;

import com.kyripay.paymentworkflow.domain.dto.ConnectionLine;

public interface BankAdapter {
    public ConnectionLine getConnectionLine(Long bankId);
}
