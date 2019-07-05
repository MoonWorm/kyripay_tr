package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;


@Value(staticConstructor="of")
@ApiModel("Format")
public class FormatDetails
{
  @ApiModelProperty("Format id. This value should be used when calling the /convert endpoint")
  private final String id;
  @ApiModelProperty("Human-readable name of the format")
  private final String name;
  @ApiModelProperty("Format description")
  private final String description;
}
