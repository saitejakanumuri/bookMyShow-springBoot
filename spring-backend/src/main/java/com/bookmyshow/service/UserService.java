package com.bookmyshow.service;

import com.bookmyshow.domain.User;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.LoginRequest;
import com.bookmyshow.dto.RegisterRequest;
import com.bookmyshow.dto.UserResponse;
import com.bookmyshow.dto.LoginResponse;
import com.bookmyshow.repository.UserRepository;
import com.bookmyshow.auth.JwtUtil;
import com.bookmyshow.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Transactional
    public ApiResponse<UserResponse> register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ApiResponse.failure("User Already exists");
        }
        String name = req.getName() != null ? req.getName().trim() : "";
        if (name.length() > 20) {
            return ApiResponse.failure("user name should be less than 20 characters");
        }
        User.Role role = parseRole(req.getRole());
        User user = User.builder()
                .name(name)
                .email(req.getEmail().trim())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .build();
        user = userRepository.save(user);
        return ApiResponse.success("User created successfully", UserResponse.from(user));
    }

    public ApiResponse<LoginResponse> login(LoginRequest req) {
        Optional<User> opt = userRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            return ApiResponse.<LoginResponse>builder().success(false).message("User does not exist. Please register").build();
        }
        User user = opt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ApiResponse.<LoginResponse>builder().success(false).message("Invalid Credentials").build();
        }
        String token = jwtUtil.generateToken(user.getId(), user.getName());
        
        return ApiResponse.success("User logged in", LoginResponse.from(user,token));
    }

    public ApiResponse<UserResponse> getCurrentUser(Long userId) {
        if (userId == null) {
            return ApiResponse.failure("Invalid token");
        }
        return userRepository.findById(userId)
                .map(u -> ApiResponse.success("you are authorized to go to the protected route", UserResponse.from(u)))
                .orElse(ApiResponse.failure("User not found"));
    }

    @Transactional
    public ApiResponse<Void> forgetPassword(String email) {
        if (email == null || email.isBlank()) {
            return ApiResponse.<Void>builder().success(false).message("Please enter the email to reset password").build();
        }
        Optional<User> opt = userRepository.findByEmail(email.trim());
        if (opt.isEmpty()) {
            return ApiResponse.<Void>builder().success(false).message("User not found").build();
        }
        User user = opt.get();
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
        user.setOtp(otp);
        user.setOtpExpiry(Instant.now().plusSeconds(10 * 60)); // 10 min
        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), user.getName(), otp);
        return ApiResponse.<Void>builder().success(true).message("otp sent on email").build();
    }

    @Transactional
    public ApiResponse<Void> resetPassword(String otp, String password) {
        if (password == null || password.isBlank() || otp == null || otp.isBlank()) {
            return ApiResponse.<Void>builder().success(false).message("invalid request").build();
        }
        Optional<User> opt = userRepository.findByOtp(otp);
        if (opt.isEmpty()) {
            return ApiResponse.<Void>builder().success(false).message("user not found").build();
        }
        User user = opt.get();
        if (user.getOtpExpiry() != null && Instant.now().isAfter(user.getOtpExpiry())) {
            return ApiResponse.<Void>builder().success(false).message("otp expired").build();
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return ApiResponse.<Void>builder().success(true).message("password reset successfully").build();
    }

    private static User.Role parseRole(String role) {
        log.info("Parsing role: {}", role);
        if (role == null || role.isBlank()) return User.Role.USER;
        try {
            return User.Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            return User.Role.USER;
        }
    }
}
