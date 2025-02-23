package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.request.CreateUserRequest;
import com.tekerasoft.arzuamber.dto.request.LoginRequest;
import com.tekerasoft.arzuamber.dto.request.RefreshTokenRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.dto.response.JwtResponse;
import com.tekerasoft.arzuamber.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(authService.register(createUserRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToke(@RequestBody RefreshTokenRequest req) {
        return ResponseEntity.ok(authService.refreshToken(req.getRefreshToken()));
    }
}
