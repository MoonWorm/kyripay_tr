/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;


/**
 * @author M-ATA
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration
{

  @Bean
  public Docket notificationApi()
  {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(basePackage(com.kyripay.notification.api.ComponentsRoot.class.getPackageName()))
        .paths(regex("/api.*"))
        .build()
        .apiInfo(getApiInfo());
  }


  private ApiInfo getApiInfo()
  {
    return new ApiInfo(
        "Notification Service API",
        "Notification Service API for KyriPay training project. API provides all the required functionality for sending notification messages to customers through different channels (email, sms...)",
        "1.0",
        "http://kyripay.com/terms_of_service",
        new Contact("Alexey Talyuk", "http://kyripay.com", "atalyuk@kyripay.com"),
        "Apache License Version 2.0",
        "https://www.apache.org/licenses/LICENSE-2.0",
        emptyList()
    );
  }

}