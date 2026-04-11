package com.hansung.logrove.global.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthContoller {

    @GetMapping("/")
    public String home() {
        return "server is running";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
