package com.example.environmental_data_service;

import com.example.environmental_data_service.config.AwsSecretsInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnvironmentalDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EnvironmentalDataServiceApplication.class);
        app.addInitializers(new AwsSecretsInitializer());
        app.run(args);
    }

}
