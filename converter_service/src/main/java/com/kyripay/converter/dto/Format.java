package com.kyripay.converter.dto;

public enum Format
{
  FORMAT_1("Format 1", 0, "Test format #1"),
  FORMAT_2("Format 2", 1, "Test format #2");


  private String name;
  private int code;
  private String description;


  Format(String name, int code, String description)
  {

    this.name = name;
    this.code = code;
    this.description = description;
  }


  public String getName()
  {
    return name;
  }


  public int getCode()
  {
    return code;
  }


  public String getDescription()
  {
    return description;
  }


}
