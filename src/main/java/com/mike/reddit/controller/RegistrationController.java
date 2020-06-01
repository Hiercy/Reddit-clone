package com.mike.reddit.controller;

import com.mike.reddit.dto.RegisterRequest;
import com.mike.reddit.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        registerService.register(registerRequest);
        return new ResponseEntity<>("User register successfully", HttpStatus.OK);
    }
}
