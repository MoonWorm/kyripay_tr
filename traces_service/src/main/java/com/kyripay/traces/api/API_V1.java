package com.kyripay.traces.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//TODO check whether it's possible to aggregate non-Spring annotation here? (io.swagger.annotations.Api for example). Will it be then recognized by Swagger?
//TODO-2: For fun: If yes - check whether it's possible to pass param from this meta-annotation to underlying (pass "API tags" for example)? Using @AliasFor (see https://stackoverflow.com/questions/10087467/passing-annotation-properties-to-meta-annotations)
public @interface API_V1 { }
