package com.kyripay.converter.service;

import com.kyripay.converter.dto.events.ConversionRequestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


public interface ConversionRequestListener
{
  @Async
  @EventListener
  void callConverter(ConversionRequestEvent request);
}
