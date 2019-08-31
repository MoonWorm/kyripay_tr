package com.kyripay.paymentworkflow.adapter;

import com.kyripay.paymentworkflow.domain.dto.ConnectionLine;
import com.kyripay.paymentworkflow.domain.service.port.out.BankAdapter;

public class BankAdapterImpl implements BankAdapter {
    @Override
    public ConnectionLine getConnectionLine(Long bankId) {
        return null;
    }
}
