package com.kyripay.converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
public class SwaggerConfig
{
  @Bean
  public Docket api()
  {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.kyripay.converter.api"))
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo()
  {
    return new ApiInfo(
        "KyriPay converter service REST API",
        "API for conversion of payments to the bank specific formats",
        "v1",
        "",
        new Contact("Alexey Stankevcih", "", "astankevich@kyriba.com"), "", "", emptyList());
  }
}
