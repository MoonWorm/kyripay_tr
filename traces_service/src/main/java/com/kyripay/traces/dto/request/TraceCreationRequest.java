/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Map;


/**
 * @author M-ASI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Trace creation request")
public class TraceCreationRequest
{
  @ApiModelProperty(value = "Payment identifier", required = true, example = "123")
  @NotNull(message = "[paymentId] can't be empty or omitted")
  @PositiveOrZero(message = "[paymentId] must be Positive value")
  private Long paymentId;

  @ApiModelProperty(value = "Trace headers map")
  private Map<@NotBlank @Size(min = 1, max = 100) String, @NotBlank @Size(min = 1, max = 255) String> headers;

}
