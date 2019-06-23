package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@ApiModel(value = "Document", description = "Converted payment document")
@AllArgsConstructor
public class Document {
    @ApiModelProperty(value = "Data format")
    private String format;
    @ApiModelProperty(value = "Status of payment conversion")
    private DocumentStatus status;
    @ApiModelProperty("Payment data in the target format")
    private byte[] data;
}
