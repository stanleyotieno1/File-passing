package com.turnover_tax;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TurnoverTaxServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TurnoverTaxServiceApplication.class, args);
        System.out.println("🚀 Turnover Tax Service started on port 8092");
    }
}