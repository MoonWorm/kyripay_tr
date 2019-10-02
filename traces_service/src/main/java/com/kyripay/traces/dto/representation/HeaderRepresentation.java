/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.dto.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * @author M-ASI
 */
@Value
@AllArgsConstructor
@ApiModel(value = "Header representation")
public class HeaderRepresentation
{
  @ApiModelProperty(value = "Header name")
  @NotBlank
  @Size(min = 1, max = 100)
  private String name;

  @ApiModelProperty(value = "Header value")
  @NotBlank
  @Size(min = 1, max = 255)
  private String value;
}
