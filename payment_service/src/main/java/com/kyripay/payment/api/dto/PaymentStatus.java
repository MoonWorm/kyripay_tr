package com.kyripay.payment.api.dto;

import com.kyripay.payment.domain.vo.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {

    @NotNull(message = "Payment status must be specified")
    @ApiModelProperty(value = "Payment status", example = "COMPLETED")
    private Status status;

}
