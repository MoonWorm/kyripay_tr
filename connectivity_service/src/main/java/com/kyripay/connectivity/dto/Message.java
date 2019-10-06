package com.kyripay.connectivity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@ApiModel(value = "Message", description = "Message to be send contains payload and all connection parameters")
public class Message {

    @Valid
    @ApiModelProperty(value = "Connection parameters to the 3rd party server")
    private ConnectionParameters connectionParameters;

    @NotEmpty
    @ApiModelProperty(value = "Base64 coded payload to be send")
    private String payload;
}
