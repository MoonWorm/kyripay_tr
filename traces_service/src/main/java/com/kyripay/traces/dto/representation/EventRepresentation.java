/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.dto.representation;

import com.kyripay.traces.domain.trace.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;


/**
 * @author M-ASI
 */
@Value(staticConstructor = "of")
@ApiModel(value = "Trace Event representation")
public class EventRepresentation
{
  @ApiModelProperty(value = "Event name")
  private String name;

  @ApiModelProperty(value = "Event creation source (origin)")
  private String source;

  @ApiModelProperty(name = "type", value = "Event creation source (origin)", allowableValues = "SUCCESS, ERROR, INFO")
  private EventType type;

  @ApiModelProperty(value = "Event comment")
  private String comment;

  @ApiModelProperty(value = "Creation timestamp")
  private LocalDateTime created;

}
