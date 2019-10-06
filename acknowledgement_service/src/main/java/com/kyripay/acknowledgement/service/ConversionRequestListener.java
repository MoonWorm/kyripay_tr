package com.kyripay.acknowledgement.service;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface ConversionRequestListener {

    @Async
    @EventListener
    void callConverter(ConversionRequestEvent conversionRequestEvent);
}
