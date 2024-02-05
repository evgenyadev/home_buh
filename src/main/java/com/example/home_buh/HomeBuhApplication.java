package com.example.home_buh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HomeBuhApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBuhApplication.class, args);
    }
}


@RestController
class MyRestController {

    @GetMapping("/")
    public String LoggedIn() {
        return "You are Logged in.";
    }
}
