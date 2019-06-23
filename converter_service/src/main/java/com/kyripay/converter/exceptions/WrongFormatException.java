package com.kyripay.converter.exceptions;

public class WrongFormatException extends RuntimeException
{
  public WrongFormatException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
