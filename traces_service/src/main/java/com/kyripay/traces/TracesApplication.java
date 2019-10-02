package com.kyripay.traces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TracesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracesApplication.class, args);
    }

}
