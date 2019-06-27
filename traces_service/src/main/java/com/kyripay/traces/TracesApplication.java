package com.kyripay.traces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
//import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
public class TracesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracesApplication.class, args);
    }

}
