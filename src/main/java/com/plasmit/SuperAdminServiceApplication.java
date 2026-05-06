package com.plasmit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.plasmit")
public class SuperAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperAdminServiceApplication.class, args);
    }
}