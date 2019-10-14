package com.kyripay.traces.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ApiModel(value = "Trace Header creation / modification request")
public class HeaderCreationUpdateRequest {
    @ApiModelProperty(value = "Header name")
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @ApiModelProperty(value = "Header value")
    @NotBlank
    @Size(min = 1, max = 255)
    private String value;
}
