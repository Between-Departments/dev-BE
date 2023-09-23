package com.gwakkili.devbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevBeApplication.class, args);
    }

}
