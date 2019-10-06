package com.kyripay.connectivity.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ApiModel(value = "Message", description = "Message to be send contains payload and all connection parameters")
public class ConnectionParameters {
    @NotBlank(message = "Url can't be empty")
    @ApiModelProperty(value = "Url to the 3rd party server")
    private String url;

    @NotBlank(message = "Login can't be empty")
    @ApiModelProperty(value = "Login to the 3rd party server")
    private String login;

    @NotBlank(message = "Password can't be empty")
    @ApiModelProperty(value = "Password to the 3rd party server")
    private String password;

    @NotNull(message = "Protocol can't be empty")
    @ApiModelProperty(value = "Protocol to be used for connection to the 3rd party server")
    private Protocol protocol;
}
