package com.kyripay.converter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Format
{
  FORMAT_1("Format 1", 0, "Test format #1"),
  FORMAT_2("Format 2", 1, "Test format #2");

  private String name;
  private int code;
  private String description;
}
