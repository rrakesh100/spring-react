package com.educative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MainClass {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
    }
}
