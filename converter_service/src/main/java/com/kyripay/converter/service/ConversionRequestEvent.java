package com.kyripay.converter.service;

import com.kyripay.converter.dto.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
class ConversionRequestEvent extends ApplicationEvent
{

  private final String documentId;
  private final String format;
  private final Payment payment;

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   * @param documentId
   * @param format
   * @param payment
   */
  ConversionRequestEvent(Object source, String documentId, String format, Payment payment)
  {
    super(source);
    this.documentId = documentId;
    this.format = format;
    this.payment = payment;
  }
}
