package com.kyripay.payment.dao.impl.jooq.mapping;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Component
public class JooqDozerConfigurationPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("mapper")) {
            DozerBeanMapper mapper = (DozerBeanMapper) bean;
            mapper.setMappingFiles(singletonList("com/kyripay/payment/dao/impl/jooq/mapping/dozerConfig.xml"));
            return mapper;
        }
        return bean;
    }

}
