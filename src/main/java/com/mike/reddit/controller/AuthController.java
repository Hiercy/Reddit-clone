package com.mike.reddit.controller;

import com.mike.reddit.dto.AuthenticationResponse;
import com.mike.reddit.dto.LoginRequest;
import com.mike.reddit.dto.RegisterRequest;
import com.mike.reddit.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // TODO: For debug. Correct it later!
        String token = authService.register(registerRequest);
        return new ResponseEntity<>("http://localhost:8080/auth/activate/" + token, HttpStatus.OK);
    }

    @GetMapping("activate/{token}")
    public ResponseEntity<String> verify(@PathVariable String token) {
        authService.verify(token);
        return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
