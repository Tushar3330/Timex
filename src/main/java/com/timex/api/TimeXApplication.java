package com.timex.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@ComponentScan(basePackages = { "com.timex.api" })
@OpenAPIDefinition(info = @Info(title = "TimeX API", version = "1.0", description = "A comprehensive time management and task tracking API"))
public class TimeXApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeXApplication.class, args);
    }
}