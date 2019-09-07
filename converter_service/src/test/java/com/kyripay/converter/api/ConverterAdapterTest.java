package com.kyripay.converter.api;

import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.service.ConversionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
public class ConverterAdapterTest
{
  @Autowired
  private Processor pipe;

  @MockBean
  ConversionService service;


  @Before
  public void setUp()
  {
    when(service.convert(any(Payment.class), eq("IDENTITY"))).thenReturn("id");
  }


  @Autowired
  private MessageCollector messageCollector;


  @Test
  public void testConversionRequestListener() throws IOException
  {
    pipe.input().send(MessageBuilder.withPayload(
        new String(getClass().getResourceAsStream("/testdata/okPayment.json").readAllBytes())
    ).build());

    verify(service).convert(any(Payment.class), eq("IDENTITY"));
  }
}
