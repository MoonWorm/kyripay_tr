/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author M-ASI
 */
@Configuration
@EnableSwagger2
public class SpringfoxConfiguration
{
  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api.*"))
        .build()
        .tags(new Tag("Traces API", "Operations related to Traces lifecycle"))
        .apiInfo(apiInfo());
  }


  private ApiInfo apiInfo()
  {
    return new ApiInfoBuilder()
        .title("KYRIPAY API DOCUMENTATION. CONFIDENTIAL. TOP SECRET.")
        .description("Traces Service REST API")
        .version("0.0.1a")
        .contact(new Contact("Alexander Sidoruk","http://kyripay.com", "asidoruk@kyriba.com"))
        .build();
  }
}
