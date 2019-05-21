package com.kyripay.acknowledgement.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@ApiModel(description = "Acknowledgement provided by 3rd party which contains status about previously sent payment")
public class Acknowledgement {
    @NotBlank(message = "Format can't be empty")
    @ApiModelProperty(value = "Acknowledgement format to be used for data conversion")
    private String format;
    @NotBlank(message = "Customer can't be empty")
    @ApiModelProperty(value = "Customer name for which we got an acknowledgement")
    private String customer;
    @NotBlank(message = "Data can't be empty")
    @ApiModelProperty(value = "Acknowledgement base64 coded data")
    private String data;
}
