package com.kyripay.connectivity.api;

import com.kyripay.connectivity.dto.Message;
import com.kyripay.connectivity.dto.Protocol;
import com.kyripay.connectivity.dto.SentMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Api("Connectivity service")
public class ConnectivityController {
    @PostMapping(value = "connection/out/{protocolId}/message")
    @ApiOperation(value = "Push message for sending to the 3rd party",
    notes = "Accepts the message with the payload to be send and all the connection properties needed for that")
    ConnectivityResponse send(@Valid @RequestBody Message message, @NotBlank(message = "Protocol id must be provided") @PathVariable Protocol protocolId){
        return new ConnectivityResponse(UUID.randomUUID().toString());
    }

    @GetMapping("connection/message/{id}")
    @ApiOperation("Returns sent message by id")
    SentMessage getMessage(@NotBlank(message = "Message is should be provided") @PathVariable String id){
        return new SentMessage(UUID.randomUUID().toString(), "OK", 123);
    }


    @Value
    @ApiModel(value = "Connectivity response", description = "Contains id of the message to be requested via '/api/v1/connection/message/{id}' endpoint")
    private class ConnectivityResponse {
        private final String messageId;
    }

}

