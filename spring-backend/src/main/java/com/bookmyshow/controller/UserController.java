package com.bookmyshow.controller;

import com.bookmyshow.dto.*;
import com.bookmyshow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Hello from BookMyShow API!");
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Received registration request for role: {}", request.getRole());
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse<LoginResponse> res = userService.login(request);
        if (!res.isSuccess()) {
            return ResponseEntity.status(401).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/get-current-user")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication auth) {
        Long userId = auth != null && auth.getPrincipal() instanceof Long ? (Long) auth.getPrincipal() : null;
        ApiResponse<UserResponse> res = userService.getCurrentUser(userId);
        if (!res.isSuccess()) {
            return ResponseEntity.status(401).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/forgetpassword")
    public ResponseEntity<ApiResponse<Void>> forgetPassword(@RequestBody(required = false) java.util.Map<String, String> body) {
        String email = body != null ? body.get("email") : null;
        ApiResponse<Void> res = userService.forgetPassword(email);
        if (!res.isSuccess() && "User not found".equals(res.getMessage())) {
            return ResponseEntity.status(404).body(res);
        }
        if (!res.isSuccess() && res.getMessage() != null && res.getMessage().contains("email")) {
            return ResponseEntity.status(401).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/resetpassword")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody(required = false) java.util.Map<String, String> body) {
        if (body == null) {
            return ResponseEntity.status(401).body(ApiResponse.failure("invalid request"));
        }
        String otp = body.get("otp");
        String password = body.get("password");
        ApiResponse<Void> res = userService.resetPassword(otp, password);
        if (!res.isSuccess() && "user not found".equals(res.getMessage())) {
            return ResponseEntity.status(404).body(res);
        }
        if (!res.isSuccess() && res.getMessage() != null && res.getMessage().contains("expired")) {
            return ResponseEntity.status(401).body(res);
        }
        return ResponseEntity.ok(res);
    }
}
