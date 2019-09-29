package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Payment", description = "Payment document to be converted to the target format")
public class Payment implements Serializable
{
  @NotBlank(message = "Payment id can't be empty")
  @ApiModelProperty(value = "Unique payment id")
  private String id;

  @ApiModelProperty(value = "Unique user id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba45")
  private String userId;

  @ApiModelProperty(value = "Payment status", example = "COMPLETED")
  private Status status;

  @ApiModelProperty(value = "Payment creation time in millis since Epoch in UTC zone", example = "12343252134")
  private long createdOn;

  @Valid
  private PaymentDetails paymentDetails;

  public enum Status {
    CREATED, PROCESSING, COMPLETED
  }
}
