package com.kyripay.payment.dao.impl.jooq.config;


import com.kyripay.payment.config.DozerConfig;
import com.kyripay.payment.dao.impl.jooq.mapping.JooqDozerConfigurationPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@DependsOn(DozerConfig.CONFIG_NAME)
@Configuration
public class JooqConfig {

    @Bean
    public JooqDozerConfigurationPostProcessor jooqDozerConfigurationPostProcessor() {
        return new JooqDozerConfigurationPostProcessor();
    }

}