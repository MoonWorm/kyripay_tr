package com.kyripay.converter.api.dto;

import com.kyripay.converter.dto.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversionRequest
{
  String format;
  Payment payment;
}
