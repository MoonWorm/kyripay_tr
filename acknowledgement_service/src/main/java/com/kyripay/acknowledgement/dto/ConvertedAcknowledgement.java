package com.kyripay.acknowledgement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel(value = "ConvertedAcknowledgement", description = "Converted acknowledgement with the info about correspondent payment")
@AllArgsConstructor
public class ConvertedAcknowledgement {
    @ApiModelProperty("The unique id of the converted acknowledgement")
    private String id;
    @ApiModelProperty(value = "The id of the initial acknowledgement")
    private String acknowledgementId;
    @ApiModelProperty("Payment transaction number")
    private String paymentTransactionNumber;
    @ApiModelProperty("Customer the payment belongs to")
    private String customer;
    @ApiModelProperty("Acknowledgement status")
    private AckStatus ackStatus;
    @ApiModelProperty("Acknowledgement level")
    private AckLevel ackLevel;
    @ApiModelProperty("Acknowledgement conversion status")
    private AckConversionStatus ackConversionStatus;
}

