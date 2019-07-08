package com.kyripay.payment.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
public class DozerConfiguration {

    @Bean
    public DozerBeanMapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(singletonList("com/kyripay/payment/config/mapping/dozerConfig.xml"));
        return mapper;
    }


}
