package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@ApiModel(value = "Document", description = "Converted payment document")
@AllArgsConstructor
public class Document {
    @ApiModelProperty("Unique id used to get converted document")
    private String id;
    @ApiModelProperty(value = "Data format")
    private Format format;
    @ApiModelProperty("Payment data in the target format")
    private byte[] data;
}
