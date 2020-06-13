package com.mike.reddit.controller;

import com.mike.reddit.dto.AuthenticationResponse;
import com.mike.reddit.dto.LoginDto;
import com.mike.reddit.dto.RegisterDto;
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
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        // TODO: For debug. Correct it later!
        String token = authService.register(registerDto);
        return new ResponseEntity<>("http://localhost:8080/auth/activate/" + token, HttpStatus.OK);
    }

    @GetMapping("activate/{token}")
    public ResponseEntity<String> verify(@PathVariable String token) {
        authService.verify(token);
        return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }
}
