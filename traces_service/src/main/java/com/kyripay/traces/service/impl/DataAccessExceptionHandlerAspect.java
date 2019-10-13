package com.kyripay.traces.service.impl;

import com.kyripay.traces.service.TracesDataAccessException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(100)
public class DataAccessExceptionHandlerAspect {

    @AfterThrowing(pointcut = "execution(* com.kyripay.traces.service.impl.TracesServiceImpl.*(..))", throwing = "e")
    public void afterThrowing(DataAccessException e) {
        throw new TracesDataAccessException(e.getMessage(), e);
    }
}
