package com.sparta.newsfeedproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NewsfeedProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsfeedProjectApplication.class, args);
    }

}
