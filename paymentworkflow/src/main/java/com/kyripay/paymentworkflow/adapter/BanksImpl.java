package com.kyripay.paymentworkflow.adapter;

import com.kyripay.paymentworkflow.domain.dto.ConnectionLine;
import com.kyripay.paymentworkflow.domain.port.out.Banks;

public class BanksImpl implements Banks {

    @Override
    public ConnectionLine getConnectionLineByBankId(Long bankId) {
        return null;
    }
}
