package com.zinkworks.services.ndoslackbotpoc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
public class SlackNotificationController {

    @GetMapping("/hello")
    public String helloEndpoint() {
        return "Hello there.";
    }
}
