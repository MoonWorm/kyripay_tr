/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support;

import com.kyripay.traces.domain.trace.ShortTraceProjection;
import com.kyripay.traces.support.trace.TraceEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
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
  TraceEventHandler traceEventHandler() {
     return new TraceEventHandler();
  }

  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer() {

    //TODO avoid using deprecated adapter - now interface methods are 'default'
    return new RepositoryRestConfigurerAdapter() {

      @Override
      public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        //DAMN! Projections in general work fine automatically (as they are located in the same package with entities), even without manual registration...
        //...unless we try to use it as "Excerpt projection". This requires manual registration here.
        //TODO double-check. Perhaps this was related to some other issues
        config.getProjectionConfiguration().addProjection(ShortTraceProjection.class);
      }


      @Override
      public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters)
      {
        super.configureHttpMessageConverters(messageConverters);

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);

        //Converter will only be used (?) if "Accept:text/plain" was requested explicitly with the request
        stringConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_PLAIN));

        //WTF? "Accept-Charset" is a Request header, why to write it into Response?
        //https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
        stringConverter.setWriteAcceptCharset(false);

        messageConverters.add(stringConverter);
      }
    };
  }
}
