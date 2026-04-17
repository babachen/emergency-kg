package com.bysj.emergencykg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@MapperScan("com.bysj.emergencykg.mapper")
@ConfigurationPropertiesScan
public class EmergencyKgApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmergencyKgApplication.class, args);
    }
}
