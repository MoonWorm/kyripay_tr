package com.kyripay.connectivity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@ApiModel(value = "SentMessage", description = "Sent message with the status and some other meta information")
public class SentMessage {
    @ApiModelProperty(value = "Unique id used to get sent message")
    @NotBlank
    private String id;

    @ApiModelProperty(value = "Status of the sent message")
    @NotBlank
    private String status;

    @ApiModelProperty(value = "Time in seconds spent to send message")
    @NotBlank
    private int elapsedTime;
}
