package com.kyripay.acknowledgement.api;

import com.kyripay.acknowledgement.api.dto.Ack;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class AcknowledgementController {
    @PostMapping("/v1/acks")
    ConvertedAcknowledgement convertAcknowledgement(@RequestBody Ack ack){
        return new ConvertedAcknowledgement("ConvertedAck-" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Value
    private static class ConvertedAcknowledgement{
        private final String id;
    }
}
