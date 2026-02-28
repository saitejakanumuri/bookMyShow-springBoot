package com.bookmyshow.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private String role; // optional, default user
}
