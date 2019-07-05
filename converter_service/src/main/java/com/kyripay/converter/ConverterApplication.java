package com.kyripay.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class ConverterApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(ConverterApplication.class, args);
  }

}
