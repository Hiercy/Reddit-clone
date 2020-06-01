package com.mike.reddit.controller;

import com.mike.reddit.dto.RegisterRequest;
import com.mike.reddit.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        registerService.register(registerRequest);
        return new ResponseEntity<>("User register successfully", HttpStatus.OK);
    }

    @GetMapping("activate/{token}")
    public ResponseEntity<String> verify(@PathVariable String token) {
        registerService.verify(token);
        return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
    }
}
