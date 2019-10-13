package com.kyripay.payment.domain.port.out.traces;

import com.kyripay.payment.domain.Trace;

public interface Traces {

    void createTrace(Trace trace);

}