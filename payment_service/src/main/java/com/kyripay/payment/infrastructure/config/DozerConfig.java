package com.kyripay.payment.infrastructure.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration(DozerConfig.CONFIG_NAME)
public class DozerConfig {

    public static final String CONFIG_NAME = "rootDozerConfiguration";

    @Bean
    public DozerBeanMapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(singletonList("com/kyripay/payment/infrastructure/config/dozerConfig.xml"));
        return mapper;
    }


}
