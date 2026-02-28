package com.bookmyshow.dto;

import com.bookmyshow.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    @JsonProperty("_id")
    private Long id;
    private String name;
    private String email;
    private String role;
    private String token;

    public static LoginResponse from(User user,String token) {
        if (user == null) return null;
        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .token(token)
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }
}
