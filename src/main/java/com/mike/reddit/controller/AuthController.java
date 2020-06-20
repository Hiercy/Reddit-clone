package com.mike.reddit.controller;

import com.mike.reddit.dto.AuthenticationResponseDto;
import com.mike.reddit.dto.LoginDto;
import com.mike.reddit.dto.RefreshTokenDto;
import com.mike.reddit.dto.RegisterDto;
import com.mike.reddit.service.AuthService;
import com.mike.reddit.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
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
    public AuthenticationResponseDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponseDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return authService.refreshToken(refreshTokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefresgToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Refresh token deleted successfully");
    }
}
