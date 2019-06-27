/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.dto.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * @author M-ASI
 */
@Data
@Builder
@RequiredArgsConstructor
@ApiModel(value = "Full Trace representation")
public class TraceRepresentation
{
  @ApiModelProperty(value = "Payment identifier")
  private final long paymentId;

  @ApiModelProperty(value = "Trace headers")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final Map<String, String> headers;

  @ApiModelProperty(value = "Trace creation timestamp")
  private final LocalDateTime created;

  @ApiModelProperty(value = "Trace update timestamp")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final LocalDateTime updated;


}
