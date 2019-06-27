/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.dto.request;

import com.kyripay.traces.domain.trace.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @author M-ASI
 */
@Data
@NoArgsConstructor
@ApiModel(value = "Trace Event creation request")
public class EventCreationRequest
{
  @NotBlank(message = "Event name must de defined")
  @ApiModelProperty(value = "Event name")
  @Size(max = 255)
  private String name;

  @ApiModelProperty(value = "Event creation source (origin)")
  @Size(max = 255)
  private String source;

  @NotNull(message = "Event message must de defined")
  @ApiModelProperty(name = "type", value = "Event creation source (origin)", allowableValues = "SUCCESS, ERROR, INFO" )
  private EventType type;

  @ApiModelProperty(value = "Event comment")
  @Size(max = 1000)
  private String comment;

}
