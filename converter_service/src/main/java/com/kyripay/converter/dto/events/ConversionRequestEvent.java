package com.kyripay.converter.dto.events;

import com.kyripay.converter.dto.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConversionRequestEvent extends ApplicationEvent
{

  private final String documentId;
  private final String format;
  private final Payment payment;

  public ConversionRequestEvent(Object source, String documentId, String format, Payment payment)
  {
    super(source);
    this.documentId = documentId;
    this.format = format;
    this.payment = payment;
  }
}
