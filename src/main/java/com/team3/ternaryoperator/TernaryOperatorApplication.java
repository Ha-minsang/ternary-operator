package com.team3.ternaryoperator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TernaryOperatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TernaryOperatorApplication.class, args);
    }

}
