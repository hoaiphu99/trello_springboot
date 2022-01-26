package com.lhoaiphu.springboottraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lhoaiphu.springboottraining")
public class SpringBootTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTrainingApplication.class, args);
    }

}
