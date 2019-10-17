package com.kyripay.converter.api.mq;

import com.kyripay.converter.api.dto.ConversionRequest;
import com.kyripay.converter.api.dto.ConverterResponse;
import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.dto.events.ConversionFinishedEvent;
import com.kyripay.converter.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;

import static org.springframework.cloud.stream.messaging.Sink.INPUT;


@EnableBinding(Processor.class)
@RequiredArgsConstructor
public class ConverterAdapter
{
  private final ConversionService conversionService;
  private final Processor pipe;

  @StreamListener(INPUT)
  public void convert(ConversionRequest request){
    try {
      conversionService.convert(request.getPayment(), request.getFormat());
    } catch (Exception e){
      pipe.output().send(MessageBuilder.withPayload
          (new ConverterResponse("N/A", DocumentStatus.CONVERSION_FAILED, e.getMessage())
          ).build());
    }
  }

  @EventListener
  public void conversionFinished(ConversionFinishedEvent event){
    pipe.output().send(MessageBuilder.withPayload(new ConverterResponse(event.getDocumentId(), DocumentStatus.READY)).build());
  }
}
