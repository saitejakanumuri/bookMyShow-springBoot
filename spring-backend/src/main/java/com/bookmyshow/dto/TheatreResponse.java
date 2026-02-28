package com.bookmyshow.dto;

import com.bookmyshow.domain.Theatre;
import com.bookmyshow.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheatreResponse {

    @JsonProperty("_id")
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private UserResponse owner;
    private Boolean isActive;

    public static TheatreResponse from(Theatre t) {
        if (t == null) return null;
        return TheatreResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .address(t.getAddress())
                .phone(t.getPhone())
                .email(t.getEmail())
                .owner(t.getOwner() != null ? UserResponse.from(t.getOwner()) : null)
                .isActive(t.getIsActive())
                .build();
    }
}
