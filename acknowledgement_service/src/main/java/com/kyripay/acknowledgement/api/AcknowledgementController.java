package com.kyripay.acknowledgement.api;

import com.kyripay.acknowledgement.dto.Acknowledgement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1")
@Api("Acknowledgement service")
public class AcknowledgementController {
    @PostMapping("acks")
    @ApiOperation(value = "Post acknowledgement for conversion",
            notes = "Accepts the acknowledgement object and convert it using converter defined by format")
    ConvertedAcknowledgement convertAcknowledgement(@Valid @RequestBody Acknowledgement acknowledgement){
        return new ConvertedAcknowledgement("ConvertedAck-" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Value
    @ApiModel(value = "Acknowledgement response", description = "Contains id of the converted acknowledgement")
    private static class ConvertedAcknowledgement{
        private final String id;
    }
}
