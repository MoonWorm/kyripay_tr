package com.kyripay.paymentworkflow.stream;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding({AcknowledgementStreams.class, ConverterStreams.class, PaymentStreams.class, ConnectorStreams.class})
public class StreamConfiguration { }
