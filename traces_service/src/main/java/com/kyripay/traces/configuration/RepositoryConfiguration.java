/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;


/**
 * @author M-ASI
 */
@Configuration
public class RepositoryConfiguration
{
  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer()
  {
    return new RepositoryRestConfigurerAdapter()
    {
      @Override
      public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters)
      {
        super.configureHttpMessageConverters(messageConverters);
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_PLAIN));
        stringConverter.setWriteAcceptCharset(false);
        messageConverters.add(stringConverter);
      }
    };
  }
}
