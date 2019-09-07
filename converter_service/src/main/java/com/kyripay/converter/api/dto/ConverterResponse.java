package com.kyripay.converter.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kyripay.converter.dto.DocumentStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Converter response", description = "Contains id of the document to be requested later")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConverterResponse
{
  @ApiModelProperty("Unique id used to get converted document")
  private final String documentId;
  private final DocumentStatus status;
  private String errorMessage;
}
