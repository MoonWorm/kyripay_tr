package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.domain.dto.ConnectionLine;

public interface Banks {
    ConnectionLine getConnectionLineByBankId(Long bankId);
}
