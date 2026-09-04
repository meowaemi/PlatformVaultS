package com.platformvaults;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.platformvaults")
@EnableScheduling
public class PlatformVaultsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformVaultsApplication.class, args);
    }
}