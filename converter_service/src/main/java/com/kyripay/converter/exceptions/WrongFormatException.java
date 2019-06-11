package com.kyripay.converter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongFormatException extends RuntimeException
{
  public WrongFormatException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
