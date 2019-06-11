package com.kyripay.converter.converters;

import com.kyripay.converter.dto.FormatDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Format
{
  IDENTITY("Identity", IdentityConverter.class, "String representation of initial payment"),
  SLOW_IDENTITY("Slow Identity", IdentityConverter.class, "String representation of initial payment returned after 10 sec.")
  ;

  private String formatName;
  private Class<? extends Converter> converterClass;
  private String description;

  public FormatDetails asFormatDetails(){
    return FormatDetails.of(name(), formatName, description);
  }
}
