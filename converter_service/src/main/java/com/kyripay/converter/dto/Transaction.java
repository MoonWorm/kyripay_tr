package com.kyripay.converter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Transaction {
  String currency;
  Float amount;
  Recipient recipient;
}