package com.kyripay.converter.dto.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConversionFinishedEvent extends ApplicationEvent
{
  private String documentId;

  public ConversionFinishedEvent(Object source, String documentId)
  {
    super(source);
    this.documentId = documentId;
  }
}
