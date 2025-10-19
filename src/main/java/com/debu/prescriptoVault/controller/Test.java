package com.debu.prescriptoVault.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/")
    public String hello() {
        return "Hello, PrescriptoVault!";
    }
}
