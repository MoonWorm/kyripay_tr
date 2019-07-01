/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;


/**
 * @author M-ASI
 */
@Data
@NoArgsConstructor
@ApiModel(value = "Trace update request")
public class TraceUpdateRequest
{
  @ApiModelProperty(value = "Trace headers")
  private Map<@NotBlank @Size(min = 1, max = 100) String, @Size(max = 255) String> headers;
}
