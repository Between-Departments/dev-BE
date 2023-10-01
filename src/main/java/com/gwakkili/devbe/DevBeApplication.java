package com.gwakkili.devbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaAuditing
@RestController
public class DevBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevBeApplication.class, args);
    }


    @GetMapping
    public String test(){
        return "Test Success!";
    }

}
